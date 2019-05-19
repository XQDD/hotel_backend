package com.zheng.hotel.dto.page;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

@Getter
@Setter
public class PageInfo {

    //仅显示一页
    public static final PageInfo ONE_PAGE_INFO = new PageInfo(Integer.MAX_VALUE);
    //仅显示一页
    public static final PageRequest ONE_PAGE = PageRequest.of(0, Integer.MAX_VALUE);
    //仅显示一条数据
    public static final PageInfo ONE_DATA = new PageInfo(1);

    @ApiModelProperty(value = "每页数目，默认值10")
    @Min(1)
    private Integer pageSize = 10;
    @ApiModelProperty(value = "当前页，默认值1")
    @Min(1)
    private Integer currPage = 1;


    public PageInfo(Integer pageSize) {
        this.pageSize = pageSize;
        init();
    }


    public PageInfo(Integer pageSize, Integer currPage) {
        this.pageSize = pageSize;
        this.currPage = currPage;
        init();
    }


    public PageInfo() {
        init();
    }

    public PageRequest getPageRequest() {
        return PageRequest.of(currPage - 1, pageSize);
    }

    public PageRequest getPageRequest(Sort.Direction direction, String... properties) {
        return PageRequest.of(currPage - 1, pageSize, direction, properties);
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null)
            pageSize = 10;
        this.pageSize = pageSize;
    }

    public void setCurrPage(Integer currPage) {
        if (currPage == null)
            currPage = 1;
        this.currPage = currPage;
    }

    private void init() {
        if (pageSize == null)
            pageSize = 10;
        if (currPage == null)
            currPage = 1;
    }
}
