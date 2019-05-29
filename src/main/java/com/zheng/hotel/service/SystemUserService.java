package com.zheng.hotel.service;

import com.zheng.hotel.bean.rbac.Permission;
import com.zheng.hotel.bean.rbac.Role;
import com.zheng.hotel.bean.rbac.SystemUser;
import com.zheng.hotel.dto.Result;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.repository.PermissionLongRepository;
import com.zheng.hotel.repository.RoleLongRepository;
import com.zheng.hotel.repository.SystemUserLongRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
        var systemUser = systemUserRepository.findByNameAndPasswordAndDeleted(name, password, false)
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

    private void initPermission() {
        //权限不存在，初始化
        if (permissionRepository.count() == 0) {
            var permissions = new ArrayList<Permission>();
            //系统用户模块
            permissions.add(new Permission("添加和修改系统用户", "sys:user:save"));
            permissions.add(new Permission("系统登录", "sys:user:login"));
            permissions.add(new Permission("添加/修改系统角色权限", "sys:user:saveRole"));
            permissions.add(new Permission("获取所有权限信息", "sys:user:getAllPermission"));
            permissions.add(new Permission("获取所有角色信息", "sys:user:getAllRole"));
            permissions.add(new Permission("获取系统用户列表", "sys:user:list"));
            permissions.add(new Permission("删除系统用户", "sys:user:delete"));

            //客房模块
            permissions.add(new Permission("添加/修改客房信息", "sys:room:save"));
            permissions.add(new Permission("获取客房标签列表", "sys:room:getRoomTags"));
            permissions.add(new Permission("获取客房列表", "sys:room:getRoomList"));
            permissions.add(new Permission("获取客房详情", "sys:room:detail"));
            permissions.add(new Permission("获取所有客房房号", "sys:room:getRoomNos"));

            //客房入住记录模块
            permissions.add(new Permission("修改入住记录状态", "sys:roomRecord:setRecordStatus"));
            permissions.add(new Permission("获取入住列表", "sys:roomRecord:getRecordList"));
            permissions.add(new Permission("客房入住", "sys:roomRecord:enter"));

            //客户模块
            permissions.add(new Permission("获取所有客户资料", "sys:customer:getAllCustomers"));
            permissions.add(new Permission("添加/更新客户资料", "sys:customer:save"));

            //通用模块
            permissions.add(new Permission("文件上传", "sys:common:upload"));

            permissionRepository.saveAll(permissions);
        }
    }

    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    public List<Permission> getAllPermission() {
        return permissionRepository.findAll();
    }

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    public void delete(long sysUserId) {
        var user = systemUserRepository.findById(sysUserId).orElseThrow(() -> Result.badRequestException("操作失败，系统用户不存在"));
        user.setDeleted(true);
        systemUserRepository.save(user);

    }

    public PageResult<SystemUser> list(PageInfo pageInfo, String keyword) {
        return new PageResult<>(systemUserRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<javax.persistence.criteria.Predicate>();
            if (StringUtils.isNotBlank(keyword)) {
                var likeKeyword = "%" + keyword + "%";
                predicates.add(cb.like(root.get("name"), likeKeyword));
            }
            predicates.add(cb.equal(root.get("deleted"), false));
            return query.where(predicates.toArray(javax.persistence.criteria.Predicate[]::new)).getRestriction();
        }, pageInfo.getPageRequest()));
    }
}
