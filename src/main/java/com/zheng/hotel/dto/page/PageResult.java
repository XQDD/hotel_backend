package com.zheng.hotel.dto.page;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.function.Function;

@Data
public class PageResult<T> {
    @ApiModelProperty(value = "分页内容", position = 0)
    private Collection<T> content;
    @ApiModelProperty(value = "当前第几页", position = 1)
    private int currPage;
    @ApiModelProperty(value = "总页数", position = 2)
    private int totalPages;
    @ApiModelProperty(value = "当前页的元素数量", position = 3)
    private int currElements;
    @ApiModelProperty(value = "每页元素个数", position = 4)
    private int pageSize;
    @ApiModelProperty(value = "所有元素个数", position = 5)
    private long totalElements;
    @ApiModelProperty(value = "是否第一页", position = 6)
    private boolean first;
    @ApiModelProperty(value = "是否最后一页", position = 7)
    private boolean last;
    @ApiModelProperty(value = "元素偏移量", position = 8)
    private long offset;
    public PageResult(Page<T> page) {
        this.content = page.getContent();
        this.pageSize = page.getSize();
        this.currPage = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.totalElements = page.getTotalElements();
        this.currElements = page.getNumberOfElements();
        this.offset = page.getPageable().getOffset();
    }
    public PageResult(Collection<T> content, long totalElements, PageInfo pageInfo) {
        this.content = content;
        this.totalElements = totalElements;
        this.pageSize = pageInfo.getPageSize();
        this.currPage = pageInfo.getCurrPage();
        //由上面计算可得
        this.totalPages = (int) (totalElements % pageSize == 0
                ? totalElements / pageSize
                : totalElements / pageSize + 1);
        this.first = currPage == 1;
        this.last = totalPages == 0 || currPage == totalPages;
        this.currElements = content.size();
        this.offset = (currPage - 1) * pageSize;
    }
    public PageResult(PageResult<?> pageResult, Collection<T> content) {
        this.totalElements = pageResult.totalElements;
        this.offset = pageResult.offset;
        this.totalPages = pageResult.totalPages;
        this.pageSize = pageResult.pageSize;
        this.currPage = pageResult.currPage;
        this.currElements = pageResult.currElements;
        this.first = pageResult.first;
        this.last = pageResult.last;
        this.content = content;
    }

    public <S> PageResult<S> transform(Function<Collection<T>, Collection<S>> function) {
        return new PageResult<>(this, function.apply(this.content));
    }

}
