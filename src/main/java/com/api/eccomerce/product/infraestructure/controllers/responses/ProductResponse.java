package com.api.eccomerce.product.infraestructure.controllers.responses;

import lombok.*;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ProductResponse {
    private String productCodeId;
    private BrandResponse brand;
    @Builder.Default private List<PriceResponse> prices = Collections.emptyList();
}
