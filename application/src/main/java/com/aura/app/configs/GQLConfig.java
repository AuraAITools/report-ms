package com.aura.app.configs;

import com.aura.app.cursor.CleanJsonKeysetCursorStrategy;
import com.aura.app.interceptors.RequestHeaderInterceptor;
import graphql.scalars.ExtendedScalars;
import graphql.validation.rules.OnValidationErrorStrategy;
import graphql.validation.rules.ValidationRules;
import graphql.validation.schemawiring.ValidationSchemaWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.query.ScrollPositionCursorStrategy;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GQLConfig {

    // import extended graphql types
    @Bean
    RuntimeWiringConfigurer runtimeWiringConfigurer() {
        ValidationSchemaWiring schemaWiring = new ValidationSchemaWiring(
                ValidationRules
                        .newValidationRules()
                        .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                        .build()
        );

        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.GraphQLLong)
                .scalar(ExtendedScalars.DateTime)
                .directiveWiring(schemaWiring)
                .build();
    }

    @Bean
    RequestHeaderInterceptor requestHeaderInterceptor() {
        return new RequestHeaderInterceptor();
    }

    @Bean
    @Primary
    CursorStrategy<ScrollPosition> scrollPositionCursorStrategy() {
        return new ScrollPositionCursorStrategy(new CleanJsonKeysetCursorStrategy());
    }
}
