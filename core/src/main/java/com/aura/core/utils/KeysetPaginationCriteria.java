package com.aura.core.utils;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Information required for keyset cursor pagination
 */
@Getter
@Setter
public class KeysetPaginationCriteria extends PaginationCriteria {
    private Map<String, ?> keyset;

    public KeysetPaginationCriteria(int pageSize, PaginationDirection direction, Map<String, ?> keyset) {
        super(pageSize, direction);
        this.keyset = keyset;
    }
}
