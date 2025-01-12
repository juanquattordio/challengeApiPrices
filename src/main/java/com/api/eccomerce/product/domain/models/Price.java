package com.api.eccomerce.product.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class Price {
    private int priceList;
    private int priority;
    private String currency;
    private Double value;
    private LocalDateTime dateTime;
}
