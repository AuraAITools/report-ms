package com.reportai.www.reportapi.api.commons;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CRUDController<T,U> {

    ResponseEntity<T> create(T resource);

    ResponseEntity<List<T>> bulkCreate(List<T> resources);

    ResponseEntity<T> getById(U id);

    ResponseEntity<List<T>> getByIds(List<U> ids);

    ResponseEntity<List<T>> getAll();

    //TODO: update implementation
    ResponseEntity<T> update(U id, T resource);

    ResponseEntity<List<T>> bulkUpdate(List<T> resource);
    ResponseEntity<Void> deleteById(U id);

    ResponseEntity<Void> deleteByIds(List<U> ids);
}
