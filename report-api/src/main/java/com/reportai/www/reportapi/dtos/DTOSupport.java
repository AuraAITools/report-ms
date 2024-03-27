package com.reportai.www.reportapi.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;

@Slf4j
public abstract class DTOSupport<T> implements DTO<T>{

    private final ModelMapper modelMapper;

    public DTOSupport(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public abstract T toEntity();

    @Override
    public <R extends DTO<T>> R of(T entity) {
        ParameterizedTypeReference<DTOSupport<T>> typedParameterClazz = new ParameterizedTypeReference<>() {};
        R DTO =  modelMapper.map(entity, typedParameterClazz.getType());
        String s;
        try {
             s = new ObjectMapper().writeValueAsString(DTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("example: {}", s);
        return DTO;
    }
}
