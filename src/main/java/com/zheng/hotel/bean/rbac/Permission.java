package com.zheng.hotel.bean.rbac;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//权限表
@Entity
@Data
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //权限外部展示名
    private String name;
    //权限内部使用名
    private String permission;

    public Permission(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }
}
