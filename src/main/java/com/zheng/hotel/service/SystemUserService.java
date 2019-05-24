package com.zheng.hotel.service;

import com.zheng.hotel.bean.rbac.Permission;
import com.zheng.hotel.bean.rbac.Role;
import com.zheng.hotel.bean.rbac.SystemUser;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.repository.PermissionLongRepository;
import com.zheng.hotel.repository.RoleLongRepository;
import com.zheng.hotel.repository.SystemUserLongRepository;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class SystemUserService {
    private final SystemUserLongRepository systemUserRepository;
    private final PermissionLongRepository permissionRepository;
    private final RoleLongRepository roleRepository;
    private final SystemOperationService systemOperationService;

    public SystemUser login(String name, String password) {
        var systemUser = systemUserRepository.findByNameAndPassword(name, password)
                .orElseThrow(() -> Result.badRequestException("账号或密码有误"));
        //判断是否有登录系统的权限
        var loginPermission = "sys:user:login";
        if (systemUser.getRoles().stream().flatMap(r -> r.getPermissions().stream().map(Permission::getPermission)).noneMatch(Predicate.isEqual(loginPermission))) {
            throw new AuthorizationException("Subject does not have permission [" + loginPermission + "]");
        } else {
            systemOperationService.recordOperation(systemUser, loginPermission);
        }
        return systemUser;
    }

    public void save(SystemUser systemUser) {
        systemUserRepository.save(systemUser);
    }

    public SystemUser getOne(Long id) {
        return systemUserRepository.getOne(id);
    }


    /**
     * 初始化用户
     */
    public void initSystemUser() {
        //超级管理员不存在，初始化
        if (!systemUserRepository.existsById(1L)) {
            //初始化权限
            initPermission();
            initRole();
            //初始化超级管理员
            var systemUser = new SystemUser();
            systemUser.
                    setName("admin");
            systemUser.setPassword("admin");
            systemUser.setRoles(List.of(roleRepository.getOne(1L)));
            systemUserRepository.save(systemUser);
        }
    }

    private void initRole() {
        //超级管理员角色不存在，初始化
        if (!roleRepository.existsById(1L)) {
            roleRepository.save(new Role("超级管理员", permissionRepository.findAll()));
        }
    }

    //TODO 完善权限系统
    private void initPermission() {
        //权限不存在，初始化
        if (permissionRepository.count() == 0) {
            var permissions = new ArrayList<Permission>();
            //用户模块
            permissions.add(new Permission("添加和修改系统用户", "sys:user:save"));
            permissions.add(new Permission("系统登录", "sys:user:login"));

            //客房模块
            permissions.add(new Permission("添加/修改客房信息", "sys:room:save"));
            permissions.add(new Permission("获取客房标签列表", "sys:room:getRoomTags"));
            permissions.add(new Permission("获取客房列表", "sys:room:getRoomList"));

            //通用模块
            permissions.add(new Permission("文件上传", "sys:common:upload"));

            permissionRepository.saveAll(permissions);
        }
    }

}
