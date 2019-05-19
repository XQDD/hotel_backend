package com.zheng.hotel.configuration.jpa;

import com.zheng.hotel.bean.rbac.SystemUser;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

//jpa审计
@Component
@RequiredArgsConstructor
public class JpaAuditorAware implements AuditorAware<SystemUser> {

    //获取当前操作的系统用户，即当前登录用户
    @Override
    public Optional<SystemUser> getCurrentAuditor() {
        return Optional.of((SystemUser) SecurityUtils.getSubject().getPrincipal());
    }
}
