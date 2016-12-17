package cn.edu.tju.dto;

import java.util.List;

public class ResponseListData extends ResponseData {
    private int page;
    private int pageSize;
    private int total;
    private String username;
    private List<ResponseLeaveApplication> list;

    public  ResponseListData(){}

    public ResponseListData(int page, int pageSize, int total, String username, List<ResponseLeaveApplication> list) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.username = username;
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ResponseLeaveApplication> getList() {
        return list;
    }

    public void setList(List<ResponseLeaveApplication> list) {
        this.list = list;
    }
}
