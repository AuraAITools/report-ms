package com.aura.core.utils;

import lombok.Data;

/**
 * Info needed for a page request
 */
@Data
public class PaginationCriteria {
    private final int pageSize;
    private final PaginationDirection direction;

    public PaginationCriteria(int pageSize, PaginationDirection direction) {
        this.pageSize = pageSize;
        this.direction = direction;
    }

    public boolean isForward() {
        return this.direction == PaginationDirection.FORWARD;
    }

    public boolean isBackward() {
        return this.direction == PaginationDirection.BACKWARD;
    }

    public enum PaginationDirection {
        FORWARD, BACKWARD
    }
}
