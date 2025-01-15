package com.api.eccomerce.product.infraestructure.controllers.responses;

import lombok.*;

@Getter
@Setter
@Builder
public class BrandResponse {
    private String brandId;
    private String description;
}
