package com.zheng.hotel.bean.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zheng.hotel.bean.base.BaseEntity;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

//客房信息
@Entity
@Data
public class Room extends BaseEntity {

    //价格（分）
    private int price;

    //保证金（分）
    private int deposit;

    //房号
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

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE} )
    private List<RoomTag> roomTags;


}
