package com.zheng.hotel.controller;

import com.zheng.hotel.bean.room.RoomRecord;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.service.RoomRecordService;
import com.zheng.hotel.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.text.ParseException;

@RestController
@Api(tags = "入住记录")
@RequestMapping(value = "roomRecord", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
public class RoomRecordController {

    private final RoomRecordService roomRecordService;

    @RequiresPermissions("sys:roomRecord:getRecordList")
    @GetMapping("getRecordList")
    @ApiOperation("获取入住列表")
    public ResponseEntity<Result<PageResult<RoomRecord>>> getRecordList(PageInfo pageInfo, String keyword, String startTime, String endTime,Integer status) throws ParseException {
        Long startDate = null;
        Long endDate = null;
        if (startTime != null) {
            startDate = TimeUtils.DATE_FORMAT.parse(startTime).getTime();
        }
        if (endTime != null) {
            endDate = TimeUtils.DATE_FORMAT.parse(endTime).getTime();
        }
        return Result.ok(roomRecordService.getRecordList(pageInfo, keyword, startDate, endDate,status));
    }

    @RequiresPermissions("sys:roomRecord:setRecordStatus")
    @GetMapping("setRecordStatus")
    @ApiOperation("修改入住记录状态")
    public ResponseEntity setRecordStatus(long recordId, @Min(0) @Max(3) int status,boolean payed) {
        roomRecordService.setRecordStatus(recordId, status,payed);
        return Result.ok();
    }

    @RequiresPermissions("sys:roomRecord:enter")
    @PostMapping("enter")
    @ApiOperation("客房入住")
    public ResponseEntity enter(@RequestBody @Valid RoomRecord roomRecord) {
        roomRecordService.enter(roomRecord);
        return Result.ok();
    }

}
