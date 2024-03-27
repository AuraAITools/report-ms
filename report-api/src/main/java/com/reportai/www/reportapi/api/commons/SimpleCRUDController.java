package com.reportai.www.reportapi.api.commons;

import com.reportai.www.reportapi.services.CRUDService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

public class SimpleCRUDController<T,U> implements CRUDController<T,U>{
    protected final CRUDService<T,U> service;

    public SimpleCRUDController(CRUDService<T, U> service) {
        this.service = service;
    }

    @PostMapping
    @Override
    public ResponseEntity<T> create(@RequestBody T resource) {
        T createdResource = service.create(resource);
        return new ResponseEntity<>(createdResource, HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    @Override
    public ResponseEntity<List<T>> bulkCreate(@RequestBody List<T> resources) {
        List<T> createdResources = service.bulkCreate(resources);
        return new ResponseEntity<>(createdResources, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<T> getById(@PathVariable U id) {
        T resource = service.findById(id);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping("/select")
    @Override
    public ResponseEntity<List<T>> getByIds(@RequestParam(value = "ids") List<U> ids) {
        List<T> resources = service.findByIds(ids);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<T>> getAll() {
        List<T> resources = service.findAll();
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable U id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/select")
    @Override
    public ResponseEntity<Void> deleteByIds(@RequestParam(name = "ids") List<U> ids) {
        service.bulkDeleteById(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<T> update(@PathVariable U id, @RequestBody T resource) {
        T result = service.findById(id);
        T updatedResource = service.update(resource);
        return new ResponseEntity<>(updatedResource,HttpStatus.OK);
    }

    @PatchMapping("/bulk")
    @Override
    public ResponseEntity<List<T>> bulkUpdate(@RequestBody List<T> resource) {
        List<T> updatedResources = service.bulkUpdate(resource);
        return new ResponseEntity<>(updatedResources, HttpStatus.OK);
    }
}
