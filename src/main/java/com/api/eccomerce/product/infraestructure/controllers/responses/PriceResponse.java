package com.api.eccomerce.product.infraestructure.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PriceResponse {
    private int priceList;
    private int priority;
    private String currency;
    private Double value;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
