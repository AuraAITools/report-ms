package com.reportai.www.reportapi.services.common;

import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of a very simple create functionality for a entity
 *
 * @param <E>
 */
public interface ISimpleCreate<E> {
    JpaRepository<E, UUID> getRepository();

    @Transactional
    default E create(E entity) {
        return getRepository().saveAndFlush(entity);
    }
}
