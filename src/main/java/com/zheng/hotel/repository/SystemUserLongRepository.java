package com.zheng.hotel.repository;

import com.zheng.hotel.bean.rbac.SystemUser;
import com.zheng.hotel.repository.base.BaseLongRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemUserLongRepository extends BaseLongRepository<SystemUser> {

    Optional<SystemUser> findByNameAndPasswordAndDeleted(String name, String password,boolean deleted);

}
