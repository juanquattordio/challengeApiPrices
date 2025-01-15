package com.api.eccomerce.product.domain.valueObjetcs;

import lombok.Getter;

@Getter
public enum Brand {
    ZARA("1", "Zara"),
    OYSHO("2", "Oysho");

    private final String id;
    private final String description;

    Brand(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Brand getBrandByBrandId(String brandId) {
        for (Brand brand : values()) {
            if (brandId.equals(brand.id)) {
                return brand;
            }
        }
        throw new IllegalArgumentException("No Brand found for brandId: " + brandId);
    }
}
