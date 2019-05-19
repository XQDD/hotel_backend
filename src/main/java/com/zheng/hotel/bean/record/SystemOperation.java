package com.zheng.hotel.bean.record;


import com.zheng.hotel.bean.rbac.Permission;
import com.zheng.hotel.bean.rbac.SystemUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SystemOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private SystemUser operator;

    @ManyToOne(optional = false)
    private Permission operation;


    @CreatedDate
    private Long createTime;

    public SystemOperation(SystemUser operator, Permission operation) {
        this.operator = operator;
        this.operation = operation;
    }
}
