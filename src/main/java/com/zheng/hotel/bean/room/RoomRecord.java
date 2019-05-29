package com.zheng.hotel.bean.room;

import com.zheng.hotel.bean.Customer;
import com.zheng.hotel.bean.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;
import java.math.BigDecimal;

@Entity
@Data
public class RoomRecord extends BaseEntity {

    @ManyToOne(optional = false)
    @Valid
    private Customer customer;

    private BigDecimal price;

    @ManyToOne(optional = false)
    @Valid
    private Room room;

    //入住时间
    private long date;

    //0预定1入住2入住完成3退房
    private int status;

    //是否支付
    private boolean payed;

    @Transient
    private String startTime;

    @Transient
    private String endTime;



}
