package com.aura.core.utils;

import java.util.Map;
import lombok.Data;

/**
 * Requested filters of a SearchRequest. filters are stored as a map of field name to filter
 */
@Data
public class SearchCriteria {
    private final Map<String, Object> filters;

    public SearchCriteria(Map<String, Object> filters) {
        this.filters = filters;
    }
}
