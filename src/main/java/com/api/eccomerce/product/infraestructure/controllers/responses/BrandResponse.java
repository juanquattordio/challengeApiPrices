package com.api.eccomerce.product.infraestructure.controllers.responses;

import static com.api.eccomerce.product.domain.value_objetcs.Brand.getBrandById;

import com.api.eccomerce.product.domain.models.exceptions.NoBrandFoundException;
import com.api.eccomerce.product.domain.value_objetcs.Brand;

import lombok.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class BrandResponse {
    private static final Logger logger = LoggerFactory.getLogger(BrandResponse.class);
    private String brandId;
    private String description;

    public BrandResponse(String brandId) {
        try {
            Brand brand = getBrandById(brandId);
            this.brandId = brand.getId();
            this.description = String.format(brand.getDescription());
        } catch (NoBrandFoundException e) {
            this.brandId = brandId;
            logger.warn("Error creating brand response: {}", e.getMessage());
        }
    }
}
