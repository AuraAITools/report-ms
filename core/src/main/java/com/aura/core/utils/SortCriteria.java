package com.aura.core.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Information required for sort request in a page request
 */
@Getter
@Setter
public class SortCriteria {
    private final String field;
    private final SortDirection direction;

    public SortCriteria(String field, SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }
}
