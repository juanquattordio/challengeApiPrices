package com.api.eccomerce.product.domain.models;

import com.api.eccomerce.product.domain.valueObjetcs.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class Product {
    private String codeId;
    private String description;
    private List<Price> prices;
    private Brand brand;
}
