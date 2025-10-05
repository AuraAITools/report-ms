package com.aura.details.utils;

import com.aura.core.utils.SearchCriteria;
import com.aura.details.tables.institutions.InstitutionTable;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

public class SearchUtils {
    private SearchUtils() {
    }

    public static Specification<InstitutionTable> buildSearchSpecification(SearchCriteria searchCriteria) {
        Map<String, Object> filters = searchCriteria.getFilters();
        Specification<InstitutionTable> spec = Specification.anyOf();
        if (filters.containsKey("name") && filters.get("name") != null && filters.get("name") instanceof String name) {
            spec = spec.or(nameIsLike(name));
        }

        if (filters.containsKey("email") && filters.get("email") != null && filters.get("email") instanceof String email) {
            spec = spec.or(emailIsLike(email));
        }
        if (filters.containsKey("contactNumber") && filters.get("contactNumber") != null && filters.get("contactNumber") instanceof String contactNumber) {
            spec = spec.or(contactNumberIsLike(contactNumber));
        }
        if (filters.containsKey("address") && filters.get("address") != null && filters.get("address") instanceof String address) {
            spec = spec.or(addressIsLike(address));
        }
        if (filters.containsKey("uen") && filters.get("uen") != null && filters.get("uen") instanceof String uen) {
            spec = spec.or(uenIsLike(uen));
        }

        return spec;
    }

    public static Specification<InstitutionTable> nameIsLike(String name) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("name")),
                "%" + name.toLowerCase() + "%"
        );
    }

    public static Specification<InstitutionTable> emailIsLike(String email) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("email")),
                "%" + email.toLowerCase() + "%"
        );
    }

    public static Specification<InstitutionTable> contactNumberIsLike(String contactNumber) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("contactNumber")),
                "%" + contactNumber.toLowerCase() + "%"
        );
    }

    public static Specification<InstitutionTable> addressIsLike(String address) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("address")),
                "%" + address.toLowerCase() + "%"
        );
    }

    public static Specification<InstitutionTable> uenIsLike(String uen) {
        return (root, query, builder) -> builder.like(
                builder.lower(root.get("uen")),
                "%" + uen.toLowerCase() + "%"
        );
    }
}
