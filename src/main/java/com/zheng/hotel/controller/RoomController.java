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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "客房")
@RequestMapping(value = "room", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
public class RoomController {

    private final RoomService roomService;

    @RequiresPermissions("sys:room:save")
    @ApiOperation("添加/修改客房信息")
    @PostMapping("save")
    public ResponseEntity save(@RequestBody  @Valid Room room) {
        roomService.save(room);
        return Result.ok();
    }

    @RequiresPermissions("sys:room:detail")
    @ApiOperation("获取客房详情")
    @GetMapping("detail")
    public ResponseEntity<Result<Optional<Room>>> detail(long id) {
        return Result.ok(roomService.findById(id));
    }


    @RequiresPermissions("sys:room:getRoomList")
    @ApiOperation("获取客房列表")
    @GetMapping("getRoomList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "order", value = "排序方式，0价格"),
            @ApiImplicitParam(name = "asc", value = "是否升序")
    })
    public ResponseEntity<Result<PageResult<Room>>> getRoomList(PageInfo pageInfo, String keyword, Integer order, Boolean asc, @RequestParam(value = "tags[]", required = false) List<String> tags, Long startTime, Long endTime) {
        return Result.ok(roomService.getRoomList(pageInfo, keyword, order, asc, tags, startTime, endTime));
    }


    @RequiresPermissions("sys:room:getRoomTags")
    @ApiOperation("获取客房标签列表")
    @GetMapping("getRoomTags")
    public ResponseEntity<Result<List<RoomTag>>> getRoomTags() {
        return Result.ok(roomService.getRoomTags());
    }


    @RequiresPermissions("sys:room:getRoomNos")
    @ApiOperation("获取所有客房房号")
    @GetMapping("getRoomNos")
    public ResponseEntity<Result<List<String>>> getRoomNos() {
        return Result.ok(roomService.getRoomNos());
    }




}
