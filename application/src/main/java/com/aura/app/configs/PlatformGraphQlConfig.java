package com.aura.app.configs;

import com.aura.app.interceptors.CorrelationIdInterceptor;
import com.aura.app.interceptors.KeycloakAuthorizationInterceptor;
import com.aura.core.clients.PolicyEnforcementService;
import graphql.scalars.ExtendedScalars;
import graphql.validation.rules.OnValidationErrorStrategy;
import graphql.validation.rules.ValidationRules;
import graphql.validation.schemawiring.ValidationSchemaWiring;
import java.io.IOException;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.data.method.annotation.support.AnnotatedControllerConfigurer;
import org.springframework.graphql.execution.DataFetcherExceptionResolver;
import org.springframework.graphql.execution.DefaultExecutionGraphQlService;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.graphql.server.WebGraphQlHandler;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.webmvc.GraphQlHttpHandler;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.POST;

@Configuration
public class PlatformGraphQlConfig {

    @Bean
    AnnotatedControllerConfigurer platformControllerConfigurer() {
        return new AnnotatedControllerConfigurer();
    }

    @Bean
    GraphQlSource platformGraphQlSource(AnnotatedControllerConfigurer platformControllerConfigurer,
                                         ResourcePatternResolver resourcePatternResolver) throws IOException {
        ValidationSchemaWiring schemaWiring = new ValidationSchemaWiring(
                ValidationRules.newValidationRules()
                        .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                        .build()
        );

        Resource[] schemas = resourcePatternResolver.getResources("classpath:graphql-platform/**/*.graphqls");
        return GraphQlSource.schemaResourceBuilder()
                .schemaResources(schemas)
                .exceptionResolvers(List.of(new GraphQlExceptionResolver()))
                .configureRuntimeWiring(wiringBuilder -> wiringBuilder
                        .scalar(ExtendedScalars.GraphQLLong)
                        .scalar(ExtendedScalars.Date)
                        .scalar(ExtendedScalars.DateTime)
                        .directiveWiring(schemaWiring))
                .configureRuntimeWiring(platformControllerConfigurer)
                .build();
    }

    @Bean
    ExecutionGraphQlService platformExecutionGraphQlService(GraphQlSource platformGraphQlSource) {
        return new DefaultExecutionGraphQlService(platformGraphQlSource);
    }

    @Bean
    WebGraphQlHandler platformWebGraphQlHandler(ExecutionGraphQlService platformExecutionGraphQlService,
                                                 PolicyEnforcementService policyEnforcementService,
                                                 ApplicationContext applicationContext) {
        List<WebGraphQlInterceptor> interceptors = List.of(
                new CorrelationIdInterceptor(),
                new KeycloakAuthorizationInterceptor(policyEnforcementService, applicationContext)
        );
        return WebGraphQlHandler.builder(platformExecutionGraphQlService)
                .interceptors(interceptors)
                .build();
    }

    @Bean
    GraphQlHttpHandler platformGraphQlHttpHandler(WebGraphQlHandler platformWebGraphQlHandler) {
        return new GraphQlHttpHandler(platformWebGraphQlHandler);
    }

    @Bean
    RouterFunction<ServerResponse> platformGraphQlRouterFunction(GraphQlHttpHandler platformGraphQlHttpHandler) {
        return RouterFunctions.route(POST("/platform/graphql"), platformGraphQlHttpHandler::handleRequest);
    }
}
