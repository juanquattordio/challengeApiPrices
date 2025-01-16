package com.api.eccomerce.product.domain.models.exceptions;

public class NoBrandFoundException extends RuntimeException {
    public NoBrandFoundException(String message) {
        super(message);
    }
}
