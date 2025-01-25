package com.reportai.www.reportapi.repositories.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TenantSpecification {

    public static <T> Specification<T> forTenant(String tenantId) {
        return (Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            if (tenantId == null) {
                return null;
            }
            return cb.equal(root.get("tenantId"), tenantId);
        };
    }
}
