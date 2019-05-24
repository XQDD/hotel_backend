package com.zheng.hotel.service;

import com.zheng.hotel.bean.room.Room;
import com.zheng.hotel.bean.room.RoomTag;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.repository.RoomRepository;
import com.zheng.hotel.repository.RoomTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomTagRepository roomTagRepository;

    public void save(Room room) {
        if (room.getId() != null) {
            //取出数据库数据，客房状态和状态描述使用数据库上的内容
            var databaseRoom = roomRepository.findById(room.getId()).orElseThrow(() -> Result.badRequestException("客房不存在"));
            room.setStatus(databaseRoom.getStatus());
            room.setStatusDescription(databaseRoom.getStatusDescription());
        }
        roomRepository.save(room);
    }


    public List<RoomTag> getRoomTags() {
        return roomTagRepository.findAll();
    }

    public PageResult<Room> getRoomList(PageInfo pageInfo, String keyword, Integer status, Integer order, Boolean asc) {
        return new PageResult<>(roomRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (keyword != null) {
                var likeKeyword = "%" + keyword + "%";
                predicates.add(cb.or(
                        //房号模糊搜索
                        cb.like(root.get("roomNo"), likeKeyword),
                        //客房标签模糊搜索
                        cb.like(root.joinList("roomTags").get("name"), likeKeyword)
                ));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
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
            return query
                    .where(predicates.toArray(Predicate[]::new))
                    .orderBy(orders)
                    .getRestriction();
        }, pageInfo.getPageRequest()));
    }
}
