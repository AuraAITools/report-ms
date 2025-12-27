package com.aura.core.utils;

import java.util.List;
import lombok.Data;

/**
 * returned results of a page
 *
 * @param <T>
 */
@Data
public class PagedResult<T> {
    private List<T> items;
    private PageInfo pageInfo;

    public PagedResult(List<T> items, PageInfo pageInfo) {
        this.items = items;
        this.pageInfo = pageInfo;
    }
}
