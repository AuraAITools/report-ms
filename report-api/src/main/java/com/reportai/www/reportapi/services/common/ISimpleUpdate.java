package com.reportai.www.reportapi.services.common;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ISimpleUpdate<E> {
    default E update(E entity) {
        return getRepository().save(entity);
    }

    JpaRepository<E, ?> getRepository();

}
