package com.zheng.hotel.controller;

import com.zheng.hotel.bean.Customer;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.service.CustomerService;
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

@RestController
@Api(tags = "客户")
@RequestMapping(value = "customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
public class CustomerController {
    private final CustomerService customerService;

    @RequiresPermissions("sys:customer:getAllCustomers")
    @ApiOperation("获取所有客户资料")
    @GetMapping("getRoomNos")
    public ResponseEntity<Result<PageResult<Customer>>> getAllCustomers(PageInfo pageInfo, String keyword) {
        return Result.ok(customerService.getAllCustomers(pageInfo, keyword));
    }

    @RequiresPermissions("sys:customer:save")
    @ApiOperation("添加/更新客户资料")
    @PostMapping("save")
    public ResponseEntity save(@RequestBody  @Valid Customer customer) {
        customerService.save(customer);
        return Result.ok();
    }

}
