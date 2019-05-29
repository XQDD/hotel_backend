package com.zheng.hotel.bean.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    //房号
    @NotBlank
    @Column(length = 100)
    private String roomNo;

    //客房说明
    private String description;

    //客房截图
    private String[] images;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RoomTag> roomTags;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<RoomRecord> roomRecords;


}
