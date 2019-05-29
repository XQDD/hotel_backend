package com.zheng.hotel.service;

import com.zheng.hotel.bean.room.RoomRecord;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.repository.CustomerRepository;
import com.zheng.hotel.repository.RoomRecordRepository;
import com.zheng.hotel.repository.RoomRepository;
import com.zheng.hotel.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RoomRecordService {
    private final RoomRecordRepository roomRecordRepository;
    private final RoomRepository roomRepository;
    private final CustomerRepository customerRepository;


    public void setRecordStatus(long recordId, int status, boolean payed) {
        var record = roomRecordRepository.findById(recordId).orElseThrow(() -> Result.badRequestException(Map.of("recordId", "记录不存在")));
        if (record.getStatus() <= status) {
            record.setStatus(status);
            record.setPayed(payed);
            roomRecordRepository.save(record);
        } else {
            throw Result.badRequestException("修改失败，不能将当前状态改为此状态");
        }
    }

    public PageResult<RoomRecord> getRecordList(PageInfo pageInfo, String keyword, Long startTime, Long endTime, Integer status) {
        return new PageResult<>(roomRecordRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (StringUtils.isNotBlank(keyword)) {
                var likeKeyWord = "%" + keyword + "%";
                var customerJoin = root.join("customer");
                predicates.add(cb.or(
                        cb.like(customerJoin.get("name"), likeKeyWord),
                        cb.like(customerJoin.get("identification"), likeKeyWord),
                        cb.like(customerJoin.get("phoneNumber"), likeKeyWord),
                        cb.like(root.join("room").get("roomNo"), likeKeyWord)
                ));
            }
            if (startTime != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), startTime));
            }
            if (endTime != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), endTime));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return query.where(predicates.toArray(Predicate[]::new)).orderBy(cb.desc(root.get("createTime"))).getRestriction();
        }, pageInfo.getPageRequest()));
    }

    public void enter(RoomRecord roomRecord) {
        try {
            //判断客房是否存在
            var room = roomRepository.findByRoomNo(roomRecord.getRoom().getRoomNo())
                    .orElseThrow(() -> Result.badRequestException("客房不存在"));
            //判断客房是否开启
            if (!room.isOpened()) {
                throw Result.badRequestException("该客房未开启");
            }
            //日期转换
            var startTime = TimeUtils.DATE_FORMAT.parse(roomRecord.getStartTime()).getTime();
            var endTime = TimeUtils.DATE_FORMAT.parse(roomRecord.getEndTime()).getTime();
            if (endTime < startTime) {
                throw Result.badRequestException("入住日期不能晚于离店日期");
            }
            //客房是否被预定
            var tempTime = startTime;
            while ((endTime - tempTime) >= 0) {
                if (roomRecordRepository.countByRoomRoomNoAndDateAndStatusNot(roomRecord.getRoom().getRoomNo(), tempTime, 3) > 0) {
                    throw Result.badRequestException("该客房在" + TimeUtils.DATE_FORMAT.format(new Date(tempTime)) + "已被预定");
                }
                tempTime += TimeUtils.DAY;
            }
            //记录生成
            tempTime = startTime;
            var customer = customerRepository.save(roomRecord.getCustomer());
            while ((endTime - tempTime) >= 0) {
                var record = new RoomRecord();
                record.setRoom(room);
                record.setCustomer(customer);
                record.setDate(tempTime);
                record.setPayed(roomRecord.isPayed());
                record.setPrice(room.getPrice());
                roomRecordRepository.save(record);
                tempTime += TimeUtils.DAY;
            }

        } catch (ParseException e) {
            throw Result.badRequestException("日期格式有误");
        }

    }
}
