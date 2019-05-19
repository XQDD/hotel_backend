package com.zheng.hotel.repository;


import com.zheng.hotel.bean.rbac.Permission;

import java.util.Optional;

public interface PermissionRepository extends BaseRepository<Permission> {
    Optional<Permission> findByPermission(String permission);
}
