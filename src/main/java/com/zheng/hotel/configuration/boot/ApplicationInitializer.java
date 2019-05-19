package com.zheng.hotel.configuration.boot;

import com.zheng.hotel.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

//启动后运行
@Component
@RequiredArgsConstructor
public class ApplicationInitializer implements ApplicationListener<ApplicationStartedEvent> {


    private final SysUserService sysUserService;


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        //初始化数据
        initData();
    }


    private void initData() {
        sysUserService.initSysUser();
    }


}
