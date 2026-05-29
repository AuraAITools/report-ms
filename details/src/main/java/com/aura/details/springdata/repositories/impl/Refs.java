package com.aura.details.springdata.repositories.impl;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Refs {
    /**
     * A builder-stub {@code XModel.builder().id(...).build()} leaves {@code @Version null} and trips
     * Hibernate's "Detached entity ... uninitialized version" check on flush. Use this helper to attach
     * a managed proxy reference instead, after MapStruct has mapped the rest of the fields.
     */
    public static <T> @Nullable T ref(EntityManager entityManager, Class<T> type, @Nullable String id) {
        return id == null ? null : entityManager.getReference(type, UUID.fromString(id));
    }
}
