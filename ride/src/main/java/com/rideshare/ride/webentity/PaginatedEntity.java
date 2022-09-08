package com.rideshare.ride.webentity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedEntity<T> {
    private List<T> nodes;
    private Integer page;
    private Integer limit;

    public PaginatedEntity() {}
}
