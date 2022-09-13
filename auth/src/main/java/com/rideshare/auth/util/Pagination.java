package com.rideshare.auth.util;

public class Pagination {
    public static int getOffset(Integer page, Integer limit) {
        if(page == null || limit == null)
            return 0;
        if(page != null && limit != null)
            return page*limit;
        return 0;
    }
}
