package com.api.eccomerce.product.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Product {
    private String codeId;
    private String description;
    private List<Price> prices;
    private String brandId;
}
