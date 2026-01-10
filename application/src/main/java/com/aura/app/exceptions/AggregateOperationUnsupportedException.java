package com.aura.app.exceptions;

import com.aura.core.exceptions.TechnicalException;

public class AggregateOperationUnsupportedException extends TechnicalException {
    public AggregateOperationUnsupportedException(String message) {
        super(message);
    }
}
