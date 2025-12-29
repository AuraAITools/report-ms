package com.aura.details.repositories.specifications;

import com.aura.core.utils.SearchCriteria;
import com.aura.details.tables.levels.LevelTable;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;


import static com.aura.details.repositories.specifications.TenantSpecification.whereTenantId;

public class LevelSpecification {
    private LevelSpecification() {
    }

    public static Specification<LevelTable> buildLevelSearchSpecification(SearchCriteria searchCriteria, String tenantId) {
        Map<String, Object> filters = searchCriteria.getFilters();
        Specification<LevelTable> spec = Specification.anyOf();
        spec = spec.and(whereTenantId(tenantId));
        if (filters.containsKey("name") && filters.get("name") != null && filters.get("name") instanceof String name) {
            spec = spec.or(nameIsLike(name));
        }
        return spec;
    }

    public static <T> Specification<T> nameIsLike(String name) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("name")),
                "%" + name.toLowerCase() + "%"
        );
    }
}
