package com.aura.core.exceptions;

public class ParentAccountAlreadyExists extends RuntimeException {
    public ParentAccountAlreadyExists(String message) {
        super(message);
    }
}
