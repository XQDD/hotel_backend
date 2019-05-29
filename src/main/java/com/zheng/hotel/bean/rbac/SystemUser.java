package com.zheng.hotel.bean.rbac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zheng.hotel.utils.CommonUtils;
import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

//用户表
@Entity
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })

public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;
    private String icon;

    //拥有的权限
    @ManyToMany
    @Valid
    private List<Role> roles;

    public String getPassword() {
        return password;
    }
    //是否删除
    private boolean deleted;
    //对密码进行加密
    public void setPassword(String password) {
        this.password = CommonUtils.encryptPassword(password);
    }
}
