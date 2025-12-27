package com.aura.details.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

public class TenantSpecification {
    private TenantSpecification() {
    }

    public static <T> Specification<T> whereTenantId(String tenantId) {
        return (root, query, builder) -> builder.equal(root.get("tenantId"), tenantId);
    }
}
