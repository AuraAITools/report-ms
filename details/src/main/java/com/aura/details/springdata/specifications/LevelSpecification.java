package com.aura.details.springdata.specifications;

import com.aura.core.utils.SearchCriteria;
import com.aura.details.springdata.models.levels.LevelModel;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

public class LevelSpecification {
    private LevelSpecification() {
    }

    public static Specification<LevelModel> buildLevelSearchSpecification(SearchCriteria searchCriteria) {
        Map<String, Object> filters = searchCriteria.getFilters();
        Specification<LevelModel> spec = Specification.anyOf();
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
