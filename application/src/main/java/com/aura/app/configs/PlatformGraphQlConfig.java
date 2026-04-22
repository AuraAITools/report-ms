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
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.graphql.data.method.annotation.support.AnnotatedControllerConfigurer;
import org.springframework.graphql.execution.DefaultBatchLoaderRegistry;
import org.springframework.graphql.execution.DefaultExecutionGraphQlService;
import org.springframework.graphql.execution.BatchLoaderRegistry;
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
    RouterFunction<ServerResponse> platformGraphQlRouterFunction(ApplicationContext applicationContext,
                                                                   ResourcePatternResolver resourcePatternResolver,
                                                                   PolicyEnforcementService policyEnforcementService) throws IOException {
        // Use a child context with its own BatchLoaderRegistry to prevent
        // @BatchMapping methods from being registered in the shared tenant registry
        GenericApplicationContext platformContext = new GenericApplicationContext();
        platformContext.setParent(applicationContext);
        platformContext.registerBean(BatchLoaderRegistry.class, (java.util.function.Supplier<BatchLoaderRegistry>) DefaultBatchLoaderRegistry::new);
        platformContext.refresh();

        AnnotatedControllerConfigurer controllerConfigurer = new AnnotatedControllerConfigurer();
        controllerConfigurer.setApplicationContext(platformContext);
        controllerConfigurer.afterPropertiesSet();

        ValidationSchemaWiring schemaWiring = new ValidationSchemaWiring(
                ValidationRules.newValidationRules()
                        .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                        .build()
        );

        Resource[] schemas = resourcePatternResolver.getResources("classpath:graphql-platform/**/*.graphqls");
        GraphQlSource graphQlSource = GraphQlSource.schemaResourceBuilder()
                .schemaResources(schemas)
                .exceptionResolvers(List.of(new GraphQlExceptionResolver()))
                .configureRuntimeWiring(wiringBuilder -> wiringBuilder
                        .scalar(ExtendedScalars.GraphQLLong)
                        .scalar(ExtendedScalars.Date)
                        .scalar(ExtendedScalars.DateTime)
                        .directiveWiring(schemaWiring))
                .configureRuntimeWiring(controllerConfigurer)
                .build();

        DefaultExecutionGraphQlService executionService = new DefaultExecutionGraphQlService(graphQlSource);

        List<WebGraphQlInterceptor> interceptors = List.of(
                new CorrelationIdInterceptor(),
                new KeycloakAuthorizationInterceptor(policyEnforcementService, applicationContext)
        );
        WebGraphQlHandler webGraphQlHandler = WebGraphQlHandler.builder(executionService)
                .interceptors(interceptors)
                .build();

        GraphQlHttpHandler handler = new GraphQlHttpHandler(webGraphQlHandler);
        return RouterFunctions.route(POST("/platform/graphql"), handler::handleRequest);
    }
}
