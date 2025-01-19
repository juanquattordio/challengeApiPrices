package com.api.eccomerce.product.infraestructure.controllers.advice;

import static com.api.eccomerce.product.domain.models.constants.PriceConstant.NEGATIVE_PRICE_VALUE_MESSAGE_ERROR;
import static com.api.eccomerce.product.infraestructure.exceptions.ExceptionMessages.BRAND_ID_BLANK_EXCEPTION;
import static com.api.eccomerce.product.infraestructure.exceptions.ExceptionMessages.INVALID_DATE_TIME_EXCEPTION;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import com.api.eccomerce.product.domain.models.exceptions.PriceException;
import com.api.eccomerce.product.infraestructure.controllers.responses.ErrorResponse;
import com.api.eccomerce.product.infraestructure.exceptions.EmptyInputException;
import com.api.eccomerce.product.infraestructure.exceptions.InvalidDateTimeFormatException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class GlobalExceptionControllerTest {
    private final GlobalExceptionController handler = new GlobalExceptionController();

    @Test
    void handlePriceException() {
        PriceException exception =
                new PriceException(NEGATIVE_PRICE_VALUE_MESSAGE_ERROR, UNPROCESSABLE_ENTITY);

        ResponseEntity<ErrorResponse> response = handler.handlePriceException(exception);

        assertEquals(UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(NEGATIVE_PRICE_VALUE_MESSAGE_ERROR, response.getBody().getMessage());
    }

    @Test
    void handleEmptyInputException() {
        EmptyInputException exception = new EmptyInputException(BRAND_ID_BLANK_EXCEPTION);

        ResponseEntity<ErrorResponse> response = handler.handleEmptyInputException(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(BRAND_ID_BLANK_EXCEPTION, response.getBody().getMessage());
    }

    @Test
    void handleEmptyInputExceptionWithoutMessageShouldCreateOne() {
        EmptyInputException exception = new EmptyInputException();

        ResponseEntity<ErrorResponse> response = handler.handleEmptyInputException(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Input must have a value", response.getBody().getMessage());
    }

    @Test
    void handleDateTimeParseException() {
        InvalidDateTimeFormatException exception =
                new InvalidDateTimeFormatException(INVALID_DATE_TIME_EXCEPTION);

        ResponseEntity<ErrorResponse> response = handler.handleDateTimeParseException(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(INVALID_DATE_TIME_EXCEPTION, response.getBody().getMessage());
    }

    @Test
    void handleGenericException() {
        Exception exception = new Exception();

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error", response.getBody().getMessage());
    }
}
