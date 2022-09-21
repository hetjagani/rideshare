package com.rideshare.payment.webentity;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedEntity<T> {
    private List<T> nodes;
    private Integer page;
    private Integer limit;

    public PaginatedEntity() {}
    public PaginatedEntity(List<T> nodes, Integer page, Integer limit) {
        this.nodes = nodes;
        if(page == null){
            this.page = 0;
        } else {
            this.page = page;
        }
        if(limit == null) {
            this.limit = 0;
        } else {
            this.limit = limit;
        }
    }
}
