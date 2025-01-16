package com.api.eccomerce.product.domain.models.exceptions;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class PriceException extends RuntimeException {
    private final HttpStatus httpStatus;

    public PriceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
