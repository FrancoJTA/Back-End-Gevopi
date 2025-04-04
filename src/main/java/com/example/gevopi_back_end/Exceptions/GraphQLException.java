package com.example.gevopi_back_end.Exceptions;

import org.springframework.graphql.execution.ErrorType;

public class GraphQLException extends RuntimeException {
    private final ErrorType errorType;

    public GraphQLException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}