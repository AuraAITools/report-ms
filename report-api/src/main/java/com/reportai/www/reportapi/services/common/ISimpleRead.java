package com.reportai.www.reportapi.services.common;

import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Implementation of simple query functions of an entity E
 *
 * @param <E>
 */
public interface ISimpleRead<E> {

    JpaRepository<E, UUID> getRepository();

    default E findById(UUID id) {
        E entity = getRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("id not found for %s resource", this.getClass().getName())));
        return entity;
    }

    default List<E> findAll() {
        List<E> foundEntities = getRepository().findAll();
        return foundEntities;
    }


    default List<E> findByIds(Collection<UUID> ids) {
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("ids cannot be empty");
        }
        List<E> entities = getRepository().findAllById(ids);
        if (ids.size() != entities.size()) {
            throw new ResourceNotFoundException("some ids could not be found");
        }
        return entities;
    }

    default E getReference(UUID id) {
        return getRepository().getReferenceById(id);
    }
}
