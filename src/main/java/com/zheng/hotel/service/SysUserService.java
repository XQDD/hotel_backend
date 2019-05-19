package com.zheng.hotel.service;

import com.zheng.hotel.bean.rbac.Permission;
import com.zheng.hotel.bean.rbac.Role;
import com.zheng.hotel.bean.rbac.SysUser;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.repository.PermissionRepository;
import com.zheng.hotel.repository.RoleRepository;
import com.zheng.hotel.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class SysUserService {
    private final SysUserRepository sysUserRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public SysUser login(String name, String password) {
        var sysUser = sysUserRepository.findByNameAndPassword(name, password)
                .orElseThrow(() -> Result.badRequestException("账号或密码有误"));
        //判断是否有登录系统的权限
        if (sysUser.getRoles().stream().flatMap(r -> r.getPermissions().stream().map(Permission::getPermission)).noneMatch(Predicate.isEqual("sys:user:login"))) {
            throw Result.badRequestException("该账号没有登录权限");
        }
        return sysUser;
    }

    public void save(SysUser sysUser) {
        sysUserRepository.save(sysUser);
    }

    public SysUser getOne(Long id) {
        return sysUserRepository.getOne(id);
    }


    /**
     * 初始化用户
     */
    public void initSysUser() {
        //超级管理员不存在，初始化
        if (!sysUserRepository.existsById(1L)) {
            //初始化权限
            initPermission();
            initRole();
            //初始化超级管理员
            var sysUser = new SysUser();
            sysUser.setName("admin");
            sysUser.setPassword("admin");
            sysUser.setRoles(List.of(roleRepository.getOne(1L)));
            sysUserRepository.save(sysUser);
        }
    }

    private void initRole() {
        //超级管理员角色不存在，初始化
        if (!roleRepository.existsById(1L)) {
            roleRepository.save(new Role("超级管理员", permissionRepository.findAll()));
        }
    }

    private void initPermission() {
        //权限不存在，初始化
        if (permissionRepository.count() == 0) {
            var permissions = new ArrayList<Permission>();
            //用户模块
            permissions.add(new Permission("添加和修改系统用户", "sys:user:save"));
            permissions.add(new Permission("系统登录", "sys:user:login"));

            //客房模块
            //TODO 完善权限系统

            permissionRepository.saveAll(permissions);
        }
    }

}
