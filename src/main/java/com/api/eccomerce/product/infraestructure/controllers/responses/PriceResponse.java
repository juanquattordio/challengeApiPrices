package com.api.eccomerce.product.infraestructure.controllers.responses;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PriceResponse {
    private int priceList;
    private int priority;
    private String currency;
    private Double value;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
