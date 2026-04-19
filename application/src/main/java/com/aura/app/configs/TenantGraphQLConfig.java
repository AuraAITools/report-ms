package com.aura.app.configs;

import com.aura.app.cursor.CleanJsonKeysetCursorStrategy;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.scalars.ExtendedScalars;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
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
public class TenantGraphQLConfig {

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
    DataFetcherExceptionResolver graphQlExceptionResolver() {
        return new GraphQlExceptionResolver();
    }

    @Bean
    @Primary
    CursorStrategy<ScrollPosition> scrollPositionCursorStrategy() {
        return new ScrollPositionCursorStrategy(new CleanJsonKeysetCursorStrategy());
    }

    // TODO: enable when ready
//    @Bean
//    Instrumentation instrumentation() {
//        return new TracingInstrumentation();
//    }
//
//    @Bean
//    Instrumentation maxQueryExecutionInstrumentation() {
//        return new MaxQueryComplexityInstrumentation(20);// each query can have max 20 fields
//    }
//
//    @Bean
//    Instrumentation maxQueryDepthInstrumentation() {
//        return new MaxQueryComplexityInstrumentation(5); // max depth of query response -> client can expand up to 5 nesting response
//    }
}
