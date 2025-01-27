package com.reportai.www.reportapi.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class MultiTenantRoleHierarchy {

    private static Map<String, Function<String, List<String>>> grantedAuthorityOperations = new HashMap<>();

    private static Set<String> expandedAuthorities = new HashSet<>();

    private static final Logger log = LoggerFactory.getLogger(MultiTenantRoleHierarchy.class);

    public static class Builder {
        public Builder() {
        }

        public Builder addHierarchy(String grantedAuthority, Function<String, List<String>> transform) {
            grantedAuthorityOperations.putIfAbsent(grantedAuthority, transform);
            return this;
        }

        public List<SimpleGrantedAuthority> createHierarchy() {
            Set<String> expandedGrantedAuthorities = new HashSet<>();
            for (Map.Entry<String, Function<String, List<String>>> operationMap : grantedAuthorityOperations.entrySet()) {
                resolve(operationMap.getKey());
            }
            return expandedAuthorities
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }
    }

    // recursively resolves a role to its lowest hierarchy
    private static void resolve(String role) {
        if (expandedAuthorities.contains(role)) {
            log.info("finished hierarchy expansion at role: {}", role);
            return;
        }
        Function<String, List<String>> operation = grantedAuthorityOperations.get(role);
        List<String> expandedAuthorities = operation.apply(role);
        expandedAuthorities.forEach(a -> {
            log.info("Resolving role {}", a);
            resolve(role);
        });
    }

}
