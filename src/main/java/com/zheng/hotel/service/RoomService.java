package com.zheng.hotel.service;

import com.zheng.hotel.bean.room.Room;
import com.zheng.hotel.bean.room.RoomTag;
import com.zheng.hotel.configuration.exception.BusinessException;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.repository.RoomRepository;
import com.zheng.hotel.repository.RoomTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomTagRepository roomTagRepository;

    @Transactional(rollbackFor = BusinessException.class)
    public void save(Room room) {
        //手动管理标签级联
        room.setRoomTags(roomTagRepository.saveAll(room.getRoomTags()));
        try {
            roomRepository.save(room);
        } catch (DataIntegrityViolationException e) {
            throw Result.badRequestException(Map.of("roomNo", "该房号已存在"));
        }
    }


    public List<RoomTag> getRoomTags() {
        return roomTagRepository.findAll();
    }

    public PageResult<Room> getRoomList(PageInfo pageInfo, String keyword, Integer order
            , Boolean asc, List<String> tags, Long startTime, Long endTime) {
        return new PageResult<>(roomRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (StringUtils.isNotBlank(keyword)) {
                var likeKeyword = "%" + keyword + "%";
                predicates.add(cb.or(
                        //房号模糊搜索
                        cb.like(root.get("roomNo"), likeKeyword),
                        //客房标签模糊搜索
                        cb.like(root.joinList("roomTags", JoinType.LEFT).get("name"), likeKeyword)
                ));
            }
            //排序
            var orders = new ArrayList<Order>();
            //排序方向，asc为true时升序，false时为降序
            var ascDirection = asc == null ? true : asc;
            //排序
            if (order != null) {
                Expression expression = null;
                //价格排序
                if (order == 0) {
                    expression = root.get("price");
                }
                if (ascDirection) {
                    orders.add(cb.asc(expression));
                } else {
                    orders.add(cb.desc(expression));
                }
            }
            //标签过滤
            if (CollectionUtils.isNotEmpty(tags)) {
                tags.forEach(tag -> predicates.add(cb.equal(root.joinList("roomTags", JoinType.LEFT).get("name"), tag)));
            }
            //筛选未被预定或已退房的房间
            if (startTime != null || endTime != null) {
                var record = root.join("roomRecords", JoinType.LEFT);
                Expression<Long> recordDate = record.get("date");
                var recordStatus = record.get("status");
                //区间
                if (startTime != null && endTime != null) {
                    predicates.add(filterRecord(cb.lessThan(recordDate, startTime), cb, recordStatus));
                    predicates.add(filterRecord(cb.greaterThan(recordDate, endTime), cb, recordStatus));
                }
                //单个日期
                else if (startTime != null) {
                    predicates.add(filterRecord(cb.notEqual(recordDate, startTime), cb, recordStatus));
                } else if (endTime != null) {
                    predicates.add(filterRecord(cb.notEqual(recordDate, endTime), cb, recordStatus));
                }
            }

            return query
                    .distinct(true)
                    .where(predicates.toArray(Predicate[]::new))
                    .orderBy(orders)
                    .getRestriction();
        }, pageInfo.getPageRequest()));
    }

    //排除状态为3(退房)和null的
    private Predicate filterRecord(Predicate predicate, CriteriaBuilder criteriaBuilder, Path path) {
        return criteriaBuilder.or(predicate, criteriaBuilder.and(criteriaBuilder.not(predicate), criteriaBuilder.or(criteriaBuilder.equal(path, 3), path.isNull())), path.isNull());
    }

    public Optional<Room> findById(long id) {
        return roomRepository.findById(id);
    }

    public List<String> getRoomNos() {
        return roomRepository.getRoomNos();
    }
}
