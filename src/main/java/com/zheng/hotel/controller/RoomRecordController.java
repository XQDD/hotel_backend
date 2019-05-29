package com.zheng.hotel.controller;

import com.zheng.hotel.bean.room.RoomRecord;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.service.RoomRecordService;
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
    public ResponseEntity<Result<PageResult<RoomRecord>>> getRecordList(PageInfo pageInfo, String keyword) {
        return Result.ok(roomRecordService.getRecordList(pageInfo, keyword));
    }

    @RequiresPermissions("sys:roomRecord:setRecordStatus")
    @PutMapping("setRecordStatus")
    @ApiOperation("修改入住记录状态")
    public ResponseEntity setRecordStatus(long recordId, @Min(1) @Max(3) int status) {
        roomRecordService.setRecordStatus(recordId, status);
        return Result.ok();
    }

    @RequiresPermissions("sys:roomRecord:enter")
    @PostMapping("enter")
    @ApiOperation("客房入住")
    public ResponseEntity enter(@RequestBody  @Valid RoomRecord roomRecord)  {
        roomRecordService.enter(roomRecord);
        return Result.ok();
    }

}
