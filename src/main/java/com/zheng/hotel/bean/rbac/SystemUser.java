package com.zheng.hotel.bean.rbac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zheng.hotel.utils.CommonUtils;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

//用户表
@Entity
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(length = 100)
    private String name;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String icon;

    //拥有的权限
    @ManyToMany
    private List<Role> roles;
    //是否删除
    private boolean deleted;

    public String getPassword() {
        return password;
    }

    //对密码进行加密
    public void setPassword(String password) {
        if (password != null) {
            this.password = CommonUtils.encryptPassword(password);
        }
    }

    public void setPasswordDirect(String password) {
        this.password = password;
    }


}
