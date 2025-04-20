package com.reportai.www.reportapi.utils;

import com.reportai.www.reportapi.entities.base.BaseEntity;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewProjectionDecorator<T extends BaseEntity, V extends BaseEntity> {

    default V project(T entity) {
        return getViewRepository().findById(entity.getId()).orElseThrow(() -> new ResourceNotFoundException(String.format("%s not found", entity.getClass().getName())));
    }

    default List<V> projectAll(List<T> entity) {
        List<V> views = getViewRepository().findAllById(entity.stream().map(T::getId).toList());
        if (views.size() != entity.size()) {
            throw new ResourceNotFoundException(String.format("some %s cannot be found", entity.getClass().getName()));
        }
        return views;
    }

    JpaRepository<V, UUID> getViewRepository();

}

