package com.zheng.hotel.bean.rbac;

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
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    //拥有的权限
    @ManyToMany
    @Valid
    private List<Role> roles;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = CommonUtils.encryptPassword(password);
    }
}
