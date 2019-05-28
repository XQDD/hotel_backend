package com.zheng.hotel.repository;

import com.zheng.hotel.bean.room.Room;
import com.zheng.hotel.repository.base.BaseLongRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends BaseLongRepository<Room> {
    @Query("select r.roomNo from Room r ")
    List<String> getRoomNos();
}
