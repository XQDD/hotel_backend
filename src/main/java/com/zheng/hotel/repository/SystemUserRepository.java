package com.zheng.hotel.repository;

import com.zheng.hotel.bean.rbac.SystemUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserRepository extends BaseRepository<SystemUser> {

    Optional<SystemUser> findByNameAndPassword(String name, String password);

}
