package com.aura.app.exceptions;

public class ResourceNotFoundBusinessException extends BusinessException {
    public ResourceNotFoundBusinessException(String resource) {
        super(resource + " not found", 404, "RESOURCE_NOT_FOUND");
    }
}
