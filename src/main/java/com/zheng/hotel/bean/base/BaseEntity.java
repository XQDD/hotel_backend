package com.zheng.hotel.bean.base;

import com.zheng.hotel.bean.rbac.SystemUser;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;


//entity基类，需要时继承
@MappedSuperclass
@Data
//使用默认审计类
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    //创建时间
    @CreatedDate
    protected Long createTime;

    //更新时间
    @LastModifiedDate
    protected Long updateTime;

    //创建人
    @CreatedBy
    @ManyToOne
    protected SystemUser createdBy;

    //最后修改人
    @LastModifiedBy
    @ManyToOne
    protected SystemUser lastModifiedBy;

}
