package com.zheng.hotel.bean.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zheng.hotel.bean.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

//客房信息
@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"roomNo"}))
public class Room extends BaseEntity {

    //价格（元）
    private BigDecimal price;

    //保证金（分）
    private BigDecimal deposit;

    //房号
    @NotBlank
    @Column(length = 100)
    private String roomNo;

    //客房说明
    private String description;

    //客房截图
    private String[] images;

    //客房状态,0待入住1已入住2已退房3清理中4未开放
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int status;

    //客房状态说明
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String statusDescription;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoomTag> roomTags;


}
