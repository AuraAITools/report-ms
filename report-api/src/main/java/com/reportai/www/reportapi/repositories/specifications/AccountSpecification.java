package com.reportai.www.reportapi.repositories.specifications;

import com.reportai.www.reportapi.entities.Account;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class AccountSpecification {
    public static Specification<Account> hasEmail(String email) {
        return (Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (email == null) {
                return null;
            }

            return builder.equal(root.get("email"), email);
        };

    }

    public static Specification<Account> firstNameLike(String firstName) {
        return (Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (firstName == null) {
                return null;
            }

            return builder.like(root.get("firstname"), "%" + firstName + "%");
        };
    }
}
