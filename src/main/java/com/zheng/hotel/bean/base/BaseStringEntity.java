package com.zheng.hotel.bean.base;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class BaseStringEntity {
    @Id
    @Column(length = 100)
    protected String name;
}
