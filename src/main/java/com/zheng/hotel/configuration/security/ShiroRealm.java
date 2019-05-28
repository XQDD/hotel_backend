package com.zheng.hotel.configuration.security;

import com.zheng.hotel.bean.rbac.Permission;
import com.zheng.hotel.bean.rbac.SystemUser;
import com.zheng.hotel.service.SystemOperationService;
import com.zheng.hotel.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;


//自定义shiro realm，设置自定义授权和认证逻辑
@RequiredArgsConstructor
@Component
public class ShiroRealm extends AuthorizingRealm {
    private final SystemUserService systemUserService;
    private final SystemOperationService systemOperationService;
    private final HttpServletRequest request;

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取登录信息
        var token = (UsernamePasswordToken) authenticationToken;
        //登录操作
        try {
            var systemUser = systemUserService.login(token.getUsername(), new String(token.getPassword()));
            //设置登录信息
            return new SimpleAuthenticationInfo(systemUser, systemUser.getPassword(), this.getName());
        } catch (RuntimeException e) {
            throw new AuthenticationException("登录失败", e);
        }
    }


    /**
     * 授权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //从shiro中获取当前已登录用户
        var systemUser = (SystemUser) principals.getPrimaryPrincipal();
        //从数据库中获取当前已登录用户权限
        var databaseUser = systemUserService.getOne(systemUser.getId());
        //设置拥有的权限
        var authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(databaseUser.getRoles().stream().flatMap(r -> r.getPermissions().stream().map(Permission::getPermission)).collect(Collectors.toList()));
        return authorizationInfo;
    }


    //判断当前用户是否有权限
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        boolean isPermitted = super.isPermitted(principals, permission);
        //成功授权，记录操作日志
        if (isPermitted && !request.getMethod().equalsIgnoreCase("OPTIONS")) {
            systemOperationService.recordOperation((SystemUser) principals.getPrimaryPrincipal(), permission);
        }
        return isPermitted;
    }
}
