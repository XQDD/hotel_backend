package com.zheng.hotel.bean.employee;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //员工名
    private String name;
    //员工性别，0男，1女
    private int sex;
    //员工联系方式
    private String contact;
    //员工图片
    private String image;
    //员工描述
    private String description;
    //员工所属部门
    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Department department;
}
