package com.aura.details.springdata.specifications;

import com.aura.core.utils.SearchCriteria;
import com.aura.details.springdata.tables.outlets.OutletTable;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

public class OutletSpecifications {
    private OutletSpecifications() {
    }

    public static Specification<OutletTable> buildOutletSearchSpecification(SearchCriteria searchCriteria) {
        Map<String, Object> filters = searchCriteria.getFilters();
        Specification<OutletTable> spec = Specification.anyOf();
        if (filters.containsKey("name") && filters.get("name") != null && filters.get("name") instanceof String name) {
            spec = spec.or(nameIsLike(name));
        }

        if (filters.containsKey("email") && filters.get("email") != null && filters.get("email") instanceof String email) {
            spec = spec.or(emailIsLike(email));
        }
        if (filters.containsKey("postalCode") && filters.get("postalCode") != null && filters.get("postalCode") instanceof String postalCode) {
            spec = spec.or(postalCodeIsLike(postalCode));
        }
        if (filters.containsKey("address") && filters.get("address") != null && filters.get("address") instanceof String address) {
            spec = spec.or(addressIsLike(address));
        }
        if (filters.containsKey("description") && filters.get("description") != null && filters.get("description") instanceof String description) {
            spec = spec.or(descriptionIsLike(description));
        }

        return spec;
    }


    public static <T> Specification<T> nameIsLike(String name) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("name")),
                "%" + name.toLowerCase() + "%"
        );
    }

    public static <T> Specification<T> emailIsLike(String email) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("email")),
                "%" + email.toLowerCase() + "%"
        );
    }

    public static <T> Specification<T> postalCodeIsLike(String postalCode) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("postalCode")),
                "%" + postalCode.toLowerCase() + "%"
        );
    }

    public static <T> Specification<T> addressIsLike(String address) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("address")),
                "%" + address.toLowerCase() + "%"
        );
    }

    public static <T> Specification<T> descriptionIsLike(String description) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("description")),
                "%" + description.toLowerCase() + "%"
        );
    }
}
