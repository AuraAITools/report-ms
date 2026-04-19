package com.aura.app.configs;

import com.aura.app.exceptions.AccessDeniedBusinessException;
import com.aura.app.exceptions.BusinessException;
import com.aura.app.exceptions.NotFoundException;
import com.aura.app.exceptions.TechnicalException;
import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.graphql.execution.ErrorType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GraphQlExceptionResolverTest {

    private GraphQlExceptionResolver resolver;

    @Mock
    private DataFetchingEnvironment env;

    @Mock
    private graphql.GraphQLContext graphQlContext;

    @Mock
    private ExecutionStepInfo executionStepInfo;

    private static final String CORRELATION_ID = "test-correlation-id";

    @BeforeEach
    void setUp() {
        resolver = new GraphQlExceptionResolver();
        when(env.getGraphQlContext()).thenReturn(graphQlContext);
        when(graphQlContext.getOrDefault("x-correlation-id", "unknown")).thenReturn(CORRELATION_ID);
        when(env.getField()).thenReturn(Field.newField("testField").build());
        when(env.getExecutionStepInfo()).thenReturn(executionStepInfo);
        when(executionStepInfo.getPath()).thenReturn(ResultPath.rootPath().segment("testField"));
    }

    @Test
    void shouldResolveBusinessExceptionWithActualMessage() {
        NotFoundException ex = new NotFoundException("Institution not found");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error).isNotNull();
        assertThat(error.getMessage()).isEqualTo("Institution not found");
        assertThat(error.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        assertThat(error.getExtensions()).containsEntry("http_status", 404);
        assertThat(error.getExtensions()).containsEntry("error_code", "NOT_FOUND");
        assertThat(error.getExtensions()).containsEntry("correlation_id", CORRELATION_ID);
    }

    @Test
    void shouldResolveAccessDeniedExceptionAsForbidden() {
        AccessDeniedBusinessException ex = new AccessDeniedBusinessException("not authorized");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error).isNotNull();
        assertThat(error.getMessage()).isEqualTo("not authorized");
        assertThat(error.getErrorType()).isEqualTo(ErrorType.FORBIDDEN);
        assertThat(error.getExtensions()).containsEntry("http_status", 403);
        assertThat(error.getExtensions()).containsEntry("error_code", "ACCESS_DENIED");
    }

    @Test
    void shouldResolveTechnicalExceptionWithMaskedMessage() {
        TechnicalException ex = new TechnicalException("NullPointerException at line 42");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error).isNotNull();
        assertThat(error.getMessage()).isEqualTo("An unexpected error occurred, please contact support");
        assertThat(error.getErrorType()).isEqualTo(ErrorType.INTERNAL_ERROR);
        assertThat(error.getExtensions()).containsEntry("http_status", 500);
        assertThat(error.getExtensions()).containsEntry("error_code", "INTERNAL_ERROR");
        assertThat(error.getExtensions()).containsEntry("correlation_id", CORRELATION_ID);
    }

    @Test
    void shouldResolveUnhandledExceptionWithMaskedMessage() {
        RuntimeException ex = new RuntimeException("something broke");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error).isNotNull();
        assertThat(error.getMessage()).isEqualTo("An unexpected error occurred, please contact support");
        assertThat(error.getErrorType()).isEqualTo(ErrorType.INTERNAL_ERROR);
        assertThat(error.getExtensions()).containsEntry("http_status", 500);
        assertThat(error.getExtensions()).containsEntry("error_code", "INTERNAL_ERROR");
    }

    @Test
    void shouldIncludeCorrelationIdInAllErrors() {
        BusinessException ex = new BusinessException("bad request", 400, "VALIDATION_FAILED");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error).isNotNull();
        assertThat(error.getExtensions()).containsEntry("correlation_id", CORRELATION_ID);
    }

    @Test
    void shouldUseUnknownWhenCorrelationIdIsMissing() {
        when(graphQlContext.getOrDefault("x-correlation-id", "unknown")).thenReturn("unknown");

        NotFoundException ex = new NotFoundException("not found");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error).isNotNull();
        assertThat(error.getExtensions()).containsEntry("correlation_id", "unknown");
    }
}
