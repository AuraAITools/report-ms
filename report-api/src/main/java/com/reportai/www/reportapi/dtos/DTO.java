package com.reportai.www.reportapi.dtos;

public interface DTO<T>{
    T toEntity();
    <R extends DTO<T>> R of(T entity);
}
