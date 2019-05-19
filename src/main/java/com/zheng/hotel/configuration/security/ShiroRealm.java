package com.zheng.hotel.configuration.security;

import com.zheng.hotel.bean.rbac.Permission;
import com.zheng.hotel.bean.rbac.SysUser;
import com.zheng.hotel.configuration.exception.BusinessException;
import com.zheng.hotel.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.stream.Collectors;


//自定义shiro realm，设置自定义授权和认证逻辑
@RequiredArgsConstructor
public class ShiroRealm extends AuthorizingRealm {
    private final SysUserService sysUserService;



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
            var sysUser = sysUserService.login(token.getUsername(), new String(token.getPassword()));
            //设置登录信息
            return new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), this.getName());
        } catch (BusinessException e) {
            throw new IncorrectCredentialsException(null, e);
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
        var sysUser = (SysUser) principals.getPrimaryPrincipal();
        //从数据库中获取当前已登录用户权限
        var databaseUser = sysUserService.getOne(sysUser.getId());
        //设置拥有的权限
        var authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(databaseUser.getRoles().stream().flatMap(r -> r.getPermissions().stream().map(Permission::getPermission)).collect(Collectors.toList()));
        return authorizationInfo;
    }

}
