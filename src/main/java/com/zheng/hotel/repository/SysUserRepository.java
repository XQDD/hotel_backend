package com.zheng.hotel.repository;

import com.zheng.hotel.bean.rbac.SysUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysUserRepository extends BaseRepository<SysUser> {

    Optional<SysUser> findByNameAndPassword(String name, String password);

}
