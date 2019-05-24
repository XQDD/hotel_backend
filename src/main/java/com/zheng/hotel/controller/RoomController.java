package com.zheng.hotel.controller;

import com.zheng.hotel.bean.room.Room;
import com.zheng.hotel.bean.room.RoomTag;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "客房")
@RequestMapping(value = "admin/activity", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @RequiresPermissions("sys:room:save")
    @ApiOperation("添加/修改客房信息")
    @PostMapping("save")
    public ResponseEntity save(@RequestBody Room room) {
        roomService.save(room);
        return Result.ok();
    }


    @RequiresPermissions("sys:room:getRoomList")
    @ApiOperation("获取客房列表")
    @PostMapping("getRoomList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", value = "排序方式，0价格"),
            @ApiImplicitParam(name = "asc", value = "是否升序")
    })
    public ResponseEntity<Result<PageResult<Room>>> getRoomList(PageInfo pageInfo, String keyword, Integer status, Integer order, Boolean asc) {
        return Result.ok(roomService.getRoomList(pageInfo, keyword, status, order, asc));
    }


    @RequiresPermissions("sys:room:getRoomTags")
    @ApiOperation("获取客房标签列表")
    @PostMapping("getRoomTags")
    public ResponseEntity<Result<List<RoomTag>>> getRoomTags(@RequestBody Room room) {
        return Result.ok(roomService.getRoomTags());
    }

}
