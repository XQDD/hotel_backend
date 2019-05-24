package com.zheng.hotel.repository;


import com.zheng.hotel.bean.rbac.Permission;
import com.zheng.hotel.repository.base.BaseLongRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PermissionLongRepository extends BaseLongRepository<Permission> {
    Optional<Permission> findByPermission(String permission);

    @Query("select p.name from Permission p where p.permission=?1")
    String findNameByPermission(String permission);
}
