package com.zheng.hotel.repository;


import com.zheng.hotel.bean.room.RoomRecord;
import com.zheng.hotel.repository.base.BaseLongRepository;

public interface RoomRecordRepository extends BaseLongRepository<RoomRecord> {


    long countByRoomRoomNoAndDateAndStatusNot(String roomNo, long date,int status);

}
