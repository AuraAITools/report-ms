package com.aura.app.controllers.graphql.v1.institutions.advice;

import com.aura.app.controllers.graphql.v1.institutions.exceptions.AccessDeniedException;
import com.aura.app.controllers.graphql.v1.institutions.exceptions.NotFoundException;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GQLGlobalExceptionHandler {

    @GraphQlExceptionHandler(NotFoundException.class)
    public GraphQLError exception(NotFoundException ex, DataFetchingEnvironment env) {
        return GraphQLError
                .newError()
                .message(ex.getMessage())
                .errorType(ErrorType.NOT_FOUND)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    @GraphQlExceptionHandler(AccessDeniedException.class)
    public GraphQLError exception(AccessDeniedException ex, DataFetchingEnvironment env) {
        return GraphQLError
                .newError()
                .message(ex.getMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

    @GraphQlExceptionHandler(RuntimeException.class)
    public GraphQLError exception(Exception ex, DataFetchingEnvironment env) {
        return GraphQLError
                .newError()
                .message(ex.getMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
    }

}
