package com.reportai.www.reportapi.services;

import java.util.List;

public interface CRUDService<T,U> {
    T create(T entity);

    List<T> bulkCreate(List<T> entities);

    T findById(U id);

    List<T> findByIds(List<U> ids);

    List<T> findAll();

    // TODO: update methods
    T update(T entity);

    List<T> bulkUpdate(List<T> entities);

    void deleteById(U id);

    void bulkDeleteById(List<U> ids);
}
