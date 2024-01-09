package com.xyhc.cms.common.page;


import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

public class PageUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 总记录数
     */
    private int total;
    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 当前页数
     */
    private int page;
    /**
     * 列表数据
     */
    private List<?> rows;

    /**
     * 返回状态
     */
    private int code;


    /**
     */
    public PageUtils(List<?> rows, int total, int pageSize, int page) {
        this.rows = rows;
        this.total = total;
        this.pageSize = pageSize;
        if (pageSize == 0) {
            pageSize = 10;
        }
        this.page = page;
//        this.totalpage = (int) Math.ceil((double) total / pageSize);
        this.code = 200;
    }

    /**
     * 分页
     */
    public PageUtils(IPage<?> page) {
        this.rows = page.getRecords();
        this.total = (int) page.getTotal();
        this.page = (int) page.getCurrent();
//        this.totalpage = (int) page.getPages();
        this.code = 200;
    }

    public PageUtils(int total, int pageSize, int page) {
        this.total = total;
        this.pageSize = pageSize;
        this.page = page;
    }
    public PageUtils(){

    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

//    public int getTotalpage() {
//        return totalpage;
//    }
//
//    public void setTotalpage(int totalpage) {
//        this.totalpage = totalpage;
//    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
