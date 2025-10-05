package com.aura.app.utils;

import com.aura.app.codegen.types.SortDirection;
import com.aura.core.utils.SortCriteria;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class SortUtil {
    private SortUtil() {
    }

    public static com.aura.core.utils.SortDirection resolveDirection(SortDirection sortDirection) {
        if (sortDirection == SortDirection.ASCENDING) {
            return com.aura.core.utils.SortDirection.ASC;
        }
        return com.aura.core.utils.SortDirection.DESC;
    }

    public static List<SortCriteria> defaultSortsWith(SortCriteria sortCriteria) {
        SortCriteria orderById = new SortCriteria("id", com.aura.core.utils.SortDirection.ASC);
        SortCriteria orderByCreatedAt = new SortCriteria("createdAt", com.aura.core.utils.SortDirection.DESC);
        Deque<SortCriteria> defaultSortCriterias = new ArrayDeque<>();
        defaultSortCriterias.addLast(orderByCreatedAt);
        defaultSortCriterias.addLast(orderById);
        // if id or createdAt is user selected sort, use user specified direction
        if ("id".equals(sortCriteria.getField())) {
            defaultSortCriterias.removeLast();
            defaultSortCriterias.addLast(new SortCriteria("id", sortCriteria.getDirection()));
            return defaultSortCriterias.stream().toList();
        }
        if ("createdAt".equals(sortCriteria.getField())) {
            defaultSortCriterias.removeFirst();
            defaultSortCriterias.addFirst(new SortCriteria("createdAt", sortCriteria.getDirection()));
            return defaultSortCriterias.stream().toList();
        }
        // if other fields are user selected sort, remove createdAt as default sort & use user specified sort
        defaultSortCriterias.removeFirst();
        defaultSortCriterias.addFirst(new SortCriteria(sortCriteria.getField(), sortCriteria.getDirection()));

        return defaultSortCriterias.stream().toList();
    }
}
