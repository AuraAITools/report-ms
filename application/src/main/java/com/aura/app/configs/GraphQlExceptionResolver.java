package com.aura.app.configs;

import com.aura.app.exceptions.BusinessException;
import com.aura.app.exceptions.TechnicalException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.security.access.AccessDeniedException;

@Slf4j
public class GraphQlExceptionResolver extends DataFetcherExceptionResolverAdapter {
    private static final String CORRELATION_ID_KEY = "x-correlation-id";
    private static final String TECHNICAL_ERROR_MESSAGE = "An unexpected error occurred, please contact support";

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        String correlationId = env.getGraphQlContext().getOrDefault(CORRELATION_ID_KEY, "unknown");

        if (ex instanceof AccessDeniedException) {
            return buildError("Access denied",
                    ErrorType.FORBIDDEN,
                    403,
                    "FORBIDDEN",
                    correlationId,
                    env);
        }

        if (ex instanceof BusinessException businessException) {
            return buildError(businessException.getMessage(),
                    mapHttpStatusToErrorType(businessException.getHttpStatus()),
                    businessException.getHttpStatus(),
                    businessException.getErrorCode(),
                    correlationId,
                    env);
        }

        if (ex instanceof TechnicalException technicalException) {
            log.error("Technical exception [correlation_id={}]: {}", correlationId, ex.getMessage(), ex);
            return buildError(TECHNICAL_ERROR_MESSAGE,
                    mapHttpStatusToErrorType(technicalException.getHttpStatus()),
                    technicalException.getHttpStatus(),
                    technicalException.getErrorCode(),
                    correlationId,
                    env);
        }

        log.error("Unhandled exception [correlation_id={}]: {}", correlationId, ex.getMessage(), ex);
        return buildError(TECHNICAL_ERROR_MESSAGE,
                ErrorType.INTERNAL_ERROR,
                500,
                "INTERNAL_ERROR",
                correlationId,
                env);
    }

    private GraphQLError buildError(String message, ErrorType errorType, int httpStatus,
                                    String errorCode, String correlationId,
                                    DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message(message)
                .errorType(errorType)
                .extensions(Map.of(
                        "http_status", httpStatus,
                        "error_code", errorCode,
                        "correlation_id", correlationId
                ))
                .build();
    }

    private ErrorType mapHttpStatusToErrorType(int httpStatus) {
        return switch (httpStatus) {
            case 400 -> ErrorType.BAD_REQUEST;
            case 401 -> ErrorType.UNAUTHORIZED;
            case 403 -> ErrorType.FORBIDDEN;
            case 404 -> ErrorType.NOT_FOUND;
            default -> ErrorType.INTERNAL_ERROR;
        };
    }
}
