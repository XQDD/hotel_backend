package com.zheng.hotel.repository;


import com.zheng.hotel.bean.room.RoomRecord;
import com.zheng.hotel.repository.base.BaseLongRepository;

import java.util.List;

public interface RoomRecordRepository extends BaseLongRepository<RoomRecord> {


    long countByRoomRoomNoAndDateAndStatusNot(String roomNo, long date,int status);

    List<RoomRecord> findByDateAndStatusNot(long date, int status);
    List<RoomRecord> findByDateGreaterThanEqualAndDateLessThanEqualAndStatusNot(long startTime,long endTime, int status);
}
