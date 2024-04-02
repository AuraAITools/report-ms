package com.reportai.www.reportapi.services.commons;

import com.reportai.www.reportapi.exceptions.NotFoundException;
import com.reportai.www.reportapi.services.commons.CRUDService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class CRUDServiceSupport<T,U> implements CRUDService<T,U> {

    protected JpaRepository<T,U> repository;

    public CRUDServiceSupport(JpaRepository<T, U> repository) {
        this.repository = repository;
    }

    @Override
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    public List<T> bulkCreate(List<T> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public T findById(U id) {
        Optional<T> result = repository.findById(id);
        if (result.isEmpty()) {
            throw new NotFoundException(String.format("%s not found",result.getClass().getName()));
        }
        return result.get();
    }

    @Override
    public List<T> findByIds(List<U> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(U id) {
        repository.deleteById(id);
    }

    @Override
    public void bulkDeleteById(List<U> ids) {
        repository.deleteAllByIdInBatch(ids);
    }

    @Override
    public T update(T entity) {
        return repository.save(entity);
    }

    @Override
    public List<T> bulkUpdate(List<T> entities) {
        return repository.saveAll(entities);
    }
}
