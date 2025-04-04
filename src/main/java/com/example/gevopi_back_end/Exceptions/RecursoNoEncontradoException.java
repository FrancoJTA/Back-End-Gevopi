package com.example.gevopi_back_end.Exceptions;

import org.springframework.graphql.execution.ErrorType;

public class RecursoNoEncontradoException extends RuntimeException {
    private final ErrorType errorType;

    // Constructor con mensaje y tipo de error
    public RecursoNoEncontradoException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    // Constructor solo con mensaje (asume NOT_FOUND por defecto)
    public RecursoNoEncontradoException(String message) {
        this(message, ErrorType.NOT_FOUND);
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}