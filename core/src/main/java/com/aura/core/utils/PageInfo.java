package com.aura.core.utils;

/**
 * Information of a Page
 */
public class PageInfo {
    private boolean hasPreviousPage;
    private boolean hasNextPage;

    public PageInfo(boolean hasPreviousPage, boolean hasNextPage) {
        this.hasPreviousPage = hasPreviousPage;
        this.hasNextPage = hasNextPage;
    }

    public boolean hasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}