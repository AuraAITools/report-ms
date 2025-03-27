package com.reportai.www.reportapi.services.common;

import com.reportai.www.reportapi.entities.base.BaseEntity;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Template for common functionalities amongst resource services
 *
 * @param <E>  resource entity
 * @param <ID> resource ID i.e. UUID
 */
public interface BaseServiceTemplate<E extends BaseEntity, ID> {

    JpaRepository<E, ID> getRepository();

    default E findById(@NonNull ID id) {
        return getRepository().findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("id not found for %s resource", this.getClass().getName())));
    }

    default List<E> findByIds(@NonNull List<ID> ids) {
        if (ids.isEmpty()) {
            throw new IllegalArgumentException("ids cannot be empty");
        }
        List<E> entities = getRepository().findAllById(ids);
        if (ids.size() != entities.size()) {
            throw new ResourceNotFoundException("some ids could not be found");
        }
        return entities;
    }

}
