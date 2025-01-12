package com.api.eccomerce.product.domain.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Brand {
    private String brandId;
    private String description;
}
