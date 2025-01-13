package com.api.eccomerce.product.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("endDateTime must not be before startDateTime");
        }

        this.priceList = priceList;
        this.priority = priority;
        this.currency = currency;
        this.value = value;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
