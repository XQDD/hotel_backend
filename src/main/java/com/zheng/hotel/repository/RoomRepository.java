package com.zheng.hotel.repository;

import com.zheng.hotel.bean.room.Room;
import com.zheng.hotel.repository.base.BaseLongRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends BaseLongRepository<Room> {
}
