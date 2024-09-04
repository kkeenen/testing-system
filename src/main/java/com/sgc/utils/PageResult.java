package com.sgc.utils;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private List<T> data; // 当前页的数据
    private int pageNo;   // 当前页码
    private int pageSize; // 每页记录数
    private long total;   // 总记录数

    public PageResult(List<T> data, int pageNo, int pageSize) {

    }

    public PageResult(List<T> data, int pageNo, int pageSize, long total) {
        this.data = data;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
    }


    public PageResult() {

    }
}
