package com.zheng.hotel.repository;

import com.zheng.hotel.bean.Customer;
import com.zheng.hotel.repository.base.BaseLongRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends BaseLongRepository<Customer> {
}
