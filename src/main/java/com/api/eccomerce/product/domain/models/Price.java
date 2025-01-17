package com.api.eccomerce.product.domain.models;

import static com.api.eccomerce.product.domain.models.constants.PriceConstant.INCOMPATIBLE_START_END_TIME_MESSAGE_ERROR;
import static com.api.eccomerce.product.domain.models.constants.PriceConstant.NEGATIVE_PRICE_VALUE_MESSAGE_ERROR;

import com.api.eccomerce.product.domain.models.exceptions.PriceException;

import lombok.Builder;
import lombok.Getter;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class Price {
    private int priceList;
    private int priority;
    private String currency;
    private Double value;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Price(
            int priceList,
            int priority,
            String currency,
            Double value,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        if (endDateTime == null || endDateTime.isBefore(startDateTime)) {
            throw new PriceException(
                    INCOMPATIBLE_START_END_TIME_MESSAGE_ERROR, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (value < 0) {
            throw new PriceException(
                    NEGATIVE_PRICE_VALUE_MESSAGE_ERROR, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        this.priceList = priceList;
        this.priority = priority;
        this.currency = currency;
        this.value = value;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
