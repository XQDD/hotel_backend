package com.zheng.hotel.service;

import com.zheng.hotel.bean.Customer;
import com.zheng.hotel.dto.page.PageInfo;
import com.zheng.hotel.dto.page.PageResult;
import com.zheng.hotel.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public PageResult<Customer> getAllCustomers(PageInfo pageInfo, String keyword) {
        return new PageResult<>(customerRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            if (StringUtils.isNotBlank(keyword)) {
                var likeKeyWord = "%" + keyword + "%";
                predicates.add(cb.or(
                        cb.like(root.get("name"), likeKeyWord),
                        cb.like(root.get("identification"), likeKeyWord),
                        cb.like(root.get("phoneNumber"), likeKeyWord)
                ));
            }
            return query.where(predicates.toArray(Predicate[]::new)).getRestriction();
        }, pageInfo.getPageRequest()));
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public Optional<Customer> detail(long customerId) {
        return customerRepository.findById(customerId);
    }

}
