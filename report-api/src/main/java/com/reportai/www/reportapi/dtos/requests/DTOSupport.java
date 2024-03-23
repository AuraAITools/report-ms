package com.reportai.www.reportapi.dtos.requests;

public interface DTOSupport<T> {

    /**
     * converts from entity to DTO
     * @param entity
     * @return
     */
    DTOSupport<T> toDTO(T entity);

    /**
     * converts from DTO to Entity
     * @return
     */
    T toEntity();
}
