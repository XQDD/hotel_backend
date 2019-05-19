package com.zheng.hotel.controller;

import com.wf.captcha.utils.CaptchaUtil;
import com.zheng.hotel.bean.rbac.SysUser;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.service.SysUserService;
import com.zheng.hotel.utils.CommonUtils;
import io.swagger.annotations.Api;
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
import java.util.Map;

@RestController
@Api(tags = "用户")
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final SysUserService sysUserService;

    private final HttpServletRequest request;

    @GetMapping("login")
    @ApiOperation("登录")
    public ResponseEntity<Result<SysUser>> login(@NotBlank String name, @NotBlank String password, @NotBlank String captcha) {
        //校验验证码
        if (!CaptchaUtil.ver(captcha, request)) {
            throw Result.badRequestException(Map.of("captcha", "验证码有误"));
        }
        //当前验证码失效处理
        CaptchaUtil.clear(request);
        //使用shiro执行登录操作
        var subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(name, CommonUtils.encryptPassword(password)));
        return Result.ok((SysUser) subject.getPrincipal());
    }

    @RequiresPermissions("sys:user:save")
    @PostMapping("save")
    @ApiOperation("添加或修改用户信息")
    public ResponseEntity save(@RequestBody @Valid SysUser sysUser) {
        sysUserService.save(sysUser);
        return Result.ok();
    }


    @GetMapping("logout")
    @ApiOperation("登出")
    public ResponseEntity logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok();
    }


    @GetMapping(value = "getCaptcha",produces = MediaType.IMAGE_PNG_VALUE)
    @ApiOperation("获取图形验证码")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        CaptchaUtil.outPng(request, response);
    }


}
