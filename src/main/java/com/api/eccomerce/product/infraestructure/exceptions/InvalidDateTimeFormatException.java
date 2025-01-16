package com.api.eccomerce.product.infraestructure.exceptions;

public class InvalidDateTimeFormatException extends RuntimeException {
    public InvalidDateTimeFormatException(String message) {
        super(message);
    }
}
