package com.example.gevopi_back_end.Exceptions;

import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

@ControllerAdvice
public class GlobalExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .errorType(ex.getErrorType()) // Usamos el errorType de la excepción
                .build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleIllegalArgumentException(IllegalArgumentException ex) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleGraphQLException(GraphQLException ex) {
        return GraphqlErrorBuilder.newError()
                .message(ex.getMessage())
                .errorType(ex.getErrorType())
                .build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleException(Exception ex) {
        return GraphqlErrorBuilder.newError()
                .message("Error interno del servidor")
                .errorType(ErrorType.INTERNAL_ERROR)
                .build();
    }
}