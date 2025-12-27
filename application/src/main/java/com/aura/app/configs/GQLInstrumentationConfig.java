package com.aura.app.configs;

import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import org.springframework.context.annotation.Bean;

// TODO: turn on this config for security
//@Profile("local")
//@Configuration
public class GQLInstrumentationConfig {
    @Bean
    Instrumentation instrumentation() {
        return new TracingInstrumentation();
    }

    @Bean
    Instrumentation maxQueryExecutionInstrumentation() {
        return new MaxQueryComplexityInstrumentation(20);// each query can have max 20 fields
    }

    @Bean
    Instrumentation maxQueryDepthInstrumentation() {
        return new MaxQueryComplexityInstrumentation(5); // max depth of query response -> client can expand up to 5 nesting response
    }
}
