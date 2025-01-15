package com.api.eccomerce.product.infraestructure.controllers.responses;

import com.api.eccomerce.product.domain.valueObjetcs.Brand;

import lombok.*;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductResponse {
    private String productCodeId;
    private BrandResponse brand;
    @Builder.Default private List<PriceResponse> prices = Collections.emptyList();
}
