package com.reportai.www.reportapi.mappers.mapper;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * takes all model mapper configurers in the application context and configures the modelMapper object
 */
@Slf4j
public class ModelMapperFactoryBean {

    private List<ModelMapperConfigurer> configurers;

    @Autowired
    public ModelMapperFactoryBean(List<ModelMapperConfigurer> configurers) {
        this.configurers = configurers;
    }

    public void configure(ModelMapper modelMapper) {
        if (configurers != null) {
            for (ModelMapperConfigurer modelMapperConfigurer : configurers) {
                log.info("configuring for {}", modelMapperConfigurer.getClass().getName());
                modelMapperConfigurer.configure(modelMapper);
            }
        }
    }

}
