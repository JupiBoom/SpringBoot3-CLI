package com.rosy.main.domain.vo;

import java.util.List;

public class RepairOrderPageVO {
    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private List<RepairOrderVO> records;

    public RepairOrderPageVO() {}

    public RepairOrderPageVO(Integer pageNum, Integer pageSize, Long total, List<RepairOrderVO> records) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.records = records;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<RepairOrderVO> getRecords() {
        return records;
    }

    public void setRecords(List<RepairOrderVO> records) {
        this.records = records;
    }
}