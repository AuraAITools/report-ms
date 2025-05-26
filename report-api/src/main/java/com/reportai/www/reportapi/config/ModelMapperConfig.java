package com.reportai.www.reportapi.config;

import com.reportai.www.reportapi.mappers.mapper.ModelMapperConfigurer;
import com.reportai.www.reportapi.mappers.mapper.ModelMapperFactoryBean;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    private final List<ModelMapperConfigurer> modelMapperConfigurers;

    @Autowired
    public ModelMapperConfig(List<ModelMapperConfigurer> modelMapperConfigurers) {
        this.modelMapperConfigurers = modelMapperConfigurers;
    }

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setPropertyCondition(mappingContext -> mappingContext.getSource() != null);
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(false)
                .setSkipNullEnabled(true)
                .setMethodAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PUBLIC);
        return modelMapper;
    }

    @Bean
    public ModelMapperFactoryBean configureModelMapper(@Autowired ModelMapper modelMapper) {
        ModelMapperFactoryBean modelMapperFactoryBean = new ModelMapperFactoryBean(modelMapperConfigurers);
        modelMapperFactoryBean.configure(modelMapper);
        return modelMapperFactoryBean;
    }
}
