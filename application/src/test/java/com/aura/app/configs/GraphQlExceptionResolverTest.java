package com.aura.app.configs;

import com.aura.app.exceptions.AccessDeniedBusinessException;
import com.aura.app.exceptions.BusinessException;
import com.aura.app.exceptions.NotFoundException;
import com.aura.app.exceptions.TechnicalException;
import com.aura.core.exceptions.LessonConflictException;
import java.util.List;
import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.execution.ResultPath;
import graphql.language.Field;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.graphql.execution.ErrorType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GraphQL exception resolver")
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
    @DisplayName("Should resolve business exception with actual message")
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
    @DisplayName("Should resolve access denied exception as forbidden")
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
    @DisplayName("Should resolve technical exception with masked message")
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
    @DisplayName("Should resolve unhandled exception with masked message")
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
    @DisplayName("Should include correlation ID in all errors")
    void shouldIncludeCorrelationIdInAllErrors() {
        BusinessException ex = new BusinessException("bad request", 400, "VALIDATION_FAILED");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error).isNotNull();
        assertThat(error.getExtensions()).containsEntry("correlation_id", CORRELATION_ID);
    }

    @Test
    @DisplayName("Should use unknown when correlation ID is missing")
    void shouldUseUnknownWhenCorrelationIdIsMissing() {
        when(graphQlContext.getOrDefault("x-correlation-id", "unknown")).thenReturn("unknown");

        NotFoundException ex = new NotFoundException("not found");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error).isNotNull();
        assertThat(error.getExtensions()).containsEntry("correlation_id", "unknown");
    }

    @Test
    @DisplayName("Should resolve room conflict with conflict_type ROOM and no subject_id")
    void shouldResolveRoomConflict() {
        LessonConflictException ex = new LessonConflictException(
                LessonConflictException.ConflictType.ROOM,
                List.of("lesson-a", "lesson-b"),
                null);

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error).isNotNull();
        assertThat(error.getMessage()).contains("Room conflict");
        assertThat(error.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        assertThat(error.getExtensions())
                .containsEntry("http_status", 409)
                .containsEntry("error_code", "LESSON_CONFLICT")
                .containsEntry("conflict_type", "ROOM")
                .containsEntry("conflicting_lesson_ids", List.of("lesson-a", "lesson-b"))
                .containsEntry("correlation_id", CORRELATION_ID)
                .doesNotContainKey("subject_id");
    }

    @Test
    @DisplayName("Should resolve student conflict with subject_id present")
    void shouldResolveStudentConflict() {
        LessonConflictException ex = new LessonConflictException(
                LessonConflictException.ConflictType.STUDENT,
                List.of("lesson-x"),
                "student-1");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error.getExtensions())
                .containsEntry("conflict_type", "STUDENT")
                .containsEntry("conflicting_lesson_ids", List.of("lesson-x"))
                .containsEntry("subject_id", "student-1");
    }

    @Test
    @DisplayName("Should resolve educator conflict with subject_id present")
    void shouldResolveEducatorConflict() {
        LessonConflictException ex = new LessonConflictException(
                LessonConflictException.ConflictType.EDUCATOR,
                List.of("lesson-y", "lesson-z"),
                "educator-1");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error.getExtensions())
                .containsEntry("conflict_type", "EDUCATOR")
                .containsEntry("conflicting_lesson_ids", List.of("lesson-y", "lesson-z"))
                .containsEntry("subject_id", "educator-1");
    }

    @Test
    @DisplayName("Should map http status 409 to BAD_REQUEST via mapHttpStatusToErrorType")
    void shouldMap409ToBadRequest() {
        BusinessException ex = new BusinessException("conflict", 409, "SOME_CONFLICT");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertThat(error.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        assertThat(error.getExtensions()).containsEntry("http_status", 409);
    }
}
