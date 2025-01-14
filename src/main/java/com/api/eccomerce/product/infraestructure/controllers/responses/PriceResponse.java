package com.api.eccomerce.product.infraestructure.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PriceResponse {
    private int priceList;
    private int priority;
    private String currency;
    private Double value;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
