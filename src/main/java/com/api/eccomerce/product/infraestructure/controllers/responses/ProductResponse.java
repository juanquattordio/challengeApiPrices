package com.api.eccomerce.product.infraestructure.controllers.responses;

import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponse {
    private String productCodeId;
    private BrandResponse brand;
    @Builder.Default private List<PriceResponse> prices = Collections.emptyList();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(productCodeId, that.productCodeId)
                && Objects.equals(brand, that.brand)
                && Objects.equals(prices, that.prices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCodeId, brand, prices);
    }
}
