package com.zheng.hotel.controller;

import com.wf.captcha.utils.CaptchaUtil;
import com.zheng.hotel.bean.rbac.Permission;
import com.zheng.hotel.bean.rbac.Role;
import com.zheng.hotel.bean.rbac.SystemUser;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.service.SystemUserService;
import com.zheng.hotel.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Api(tags = "用户")
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
public class SystemUserController {

    private final SystemUserService systemUserService;
    private final HttpServletRequest request;

    @GetMapping("login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", paramType = "query"),
            @ApiImplicitParam(name = "password", paramType = "query"),
            @ApiImplicitParam(name = "captcha", paramType = "query"),
    })
    public ResponseEntity<Result<SystemUser>> login(@NotBlank String name, @NotBlank String password, @NotBlank String captcha) {
        //校验验证码
        //TODO 演示时加上验证码校验
        if (!CaptchaUtil.ver(captcha, request)
                && false
        ) {
            throw Result.badRequestException(Map.of("captcha", "验证码有误"));
        }
        //当前验证码失效处理
        CaptchaUtil.clear(request);
        //使用shiro执行登录操作
        var subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(name, CommonUtils.encryptPassword(password)));
        return Result.ok((SystemUser) subject.getPrincipal());
    }


    @GetMapping("logout")
    @ApiOperation("登出")
    public ResponseEntity logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok();
    }


    @GetMapping(value = "getCaptcha", produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperation("获取图形验证码")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        CaptchaUtil.outPng(request, response);
    }


    @RequiresPermissions("sys:user:save")
    @PostMapping("save")
    @ApiOperation("添加或修改用户信息")
    public ResponseEntity save(@RequestBody @Valid SystemUser systemUser) {
        if (systemUser.getId() != null && systemUser.getId() == 1) {
            if (systemUser.getRoles() == null || systemUser.getRoles().size() != 1 || systemUser.getRoles().get(0).getId() == null || systemUser.getRoles().get(0).getId() != 1)
                throw Result.badRequestException("系统初始超级管理员拥有的角色不可更改");
        }
        if (systemUser.getId() == null && systemUser.getPassword() == null) {
            throw Result.badRequestException(Map.of("password", "不能为空"));
        }
        systemUserService.save(systemUser);
        return Result.ok();
    }


    @RequiresPermissions("sys:user:saveRole")
    @PostMapping("saveRole")
    @ApiOperation("添加/修改系统角色权限")
    public ResponseEntity saveRole(@RequestBody @Valid Role role) {
        if (role.getId() != null && role.getId() == 1) {
            throw Result.badRequestException("系统初始角色不可更改");
        }
        systemUserService.saveRole(role);
        return Result.ok();
    }


    @RequiresPermissions("sys:user:getAllPermission")
    @GetMapping("getAllPermission")
    @ApiOperation("获取所有权限信息")
    public ResponseEntity<Result<List<Permission>>> getAllPermission() {
        return Result.ok(systemUserService.getAllPermission());
    }

    @RequiresPermissions("sys:user:getAllRole")
    @GetMapping("getAllRole")
    @ApiOperation("获取所有角色信息")
    public ResponseEntity<Result<List<Role>>> getAllRole(String keyword) {
        return Result.ok(systemUserService.getAllRole(keyword));
    }


    @RequiresPermissions("sys:user:delete")
    @GetMapping("delete")
    @ApiOperation("删除系统用户")
    public ResponseEntity delete(long sysUserId) {
        if (sysUserId == 1) {
            throw Result.badRequestException("系统初始超级管理员不能删除");
        }
        systemUserService.delete(sysUserId);
        return Result.ok();
    }


    @RequiresPermissions("sys:user:list")
    @GetMapping("list")
    @ApiOperation("获取系统用户列表")
    public ResponseEntity<Result<PageResult<SystemUser>>> list(PageInfo pageInfo, String keyword, @RequestParam(value = "roles[]", required = false) List<Long> roles) {
        return Result.ok(systemUserService.list(pageInfo, keyword, roles));
    }


    @RequiresPermissions("sys:user:detail")
    @GetMapping("detail")
    @ApiOperation("获取系统用户详情")
    public ResponseEntity<Result<Optional<SystemUser>>> detail(long sysUserId) {
        return Result.ok(systemUserService.detail(sysUserId));
    }


    @RequiresPermissions("sys:user:deleteRole")
    @GetMapping("deleteRole")
    @ApiOperation("删除系统角色")
    public ResponseEntity deleteRole(long roleId) {
        if (roleId == 1) {
            throw Result.badRequestException("系统初始超级管理员角色不能删除");
        }
        systemUserService.deleteRole(roleId);
        return Result.ok();
    }

}
