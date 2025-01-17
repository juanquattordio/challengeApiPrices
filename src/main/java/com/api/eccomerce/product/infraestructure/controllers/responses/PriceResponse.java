package com.api.eccomerce.product.infraestructure.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PriceResponse {
    private int priceList;
    private int priority;
    private String currency;
    private Double value;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceResponse that = (PriceResponse) o;
        return priceList == that.priceList
                && priority == that.priority
                && Double.compare(that.value, value) == 0
                && Objects.equals(currency, that.currency)
                && Objects.equals(startDateTime, that.startDateTime)
                && Objects.equals(endDateTime, that.endDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(priceList, priority, currency, value, startDateTime, endDateTime);
    }
}
