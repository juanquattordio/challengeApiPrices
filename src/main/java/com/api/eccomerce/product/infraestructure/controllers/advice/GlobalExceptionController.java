package com.api.eccomerce.product.infraestructure.controllers.advice;

import com.api.eccomerce.product.domain.models.exceptions.PriceException;
import com.api.eccomerce.product.infraestructure.controllers.responses.ErrorResponse;
import com.api.eccomerce.product.infraestructure.exceptions.EmptyInputException;
import com.api.eccomerce.product.infraestructure.exceptions.InvalidDateTimeFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

    /**/
    /*Domain exception handlers*/
    /**/

    @ExceptionHandler(PriceException.class)
    public ResponseEntity<ErrorResponse> handlePriceException(PriceException ex) {
        logError(ex);
        ErrorResponse errorResponse =
                new ErrorResponse(ex.getHttpStatus().value(), ex.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }

    /**/
    /*Infrastructure exception handlers*/
    /**/

    @ExceptionHandler(EmptyInputException.class)
    public ResponseEntity<ErrorResponse> handleEmptyInputException(EmptyInputException ex) {
        logError(ex);
        ErrorResponse errorResponse =
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage() == null ? "Input must have a value" : ex.getMessage(),
                        LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateTimeFormatException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(
            InvalidDateTimeFormatException ex) {
        logError(ex);
        ErrorResponse errorResponse =
                new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(), ex.getMessage(), LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logError(ex);
        ErrorResponse errorResponse =
                new ErrorResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Unexpected error",
                        LocalDateTime.now());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logError(Exception ex) {
        logger.error("Exception: {}", ex.getMessage(), ex);
    }
}
