package com.starnetsecurity.common.dto;

import java.util.Collections;
import java.util.List;

/**
 * Created by 宏炜 on 2017-06-21.
 */
public class DataTablePageInfo {

    private List data;
    private Long count;

    public DataTablePageInfo() {
        data = Collections.EMPTY_LIST;
        count = 0L;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
