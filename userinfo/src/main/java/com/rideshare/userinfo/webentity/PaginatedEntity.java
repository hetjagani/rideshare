package com.rideshare.userinfo.webentity;

import java.util.List;

public class PaginatedEntity<T> {
    private List<T> nodes;
    private Integer page;
    private Integer limit;

    public PaginatedEntity () {}

    public PaginatedEntity(List<T> nodes, Integer page, Integer limit) {
        this.nodes = nodes;
        this.page = page;
        this.limit = limit;
    }

    public List<T> getNodes() {
        return nodes;
    }

    public void setNodes(List<T> nodes) {
        this.nodes = nodes;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
