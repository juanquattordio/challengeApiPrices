package com.api.eccomerce.product.domain.value_objetcs;

import com.api.eccomerce.product.domain.models.exceptions.NoBrandFoundException;

import lombok.Getter;

@Getter
public enum Brand {
    ZARA("1", "Zara"),
    OYSHO("2", "Oysho"),
    PULL_AND_BEAR("3", "Pull&Bear"),
    MASSIMO("4", "Massimo Dutti"),
    BERSHKA("5", "Bershka"),
    STRADIVARIUS("6", "Stradivarius"),
    ZARA_HOME("5", "Zara Home");

    private final String id;
    private final String description;

    Brand(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public static Brand getBrandById(String brandId) {
        for (Brand brand : values()) {
            if (brandId.equals(brand.id)) {
                return brand;
            }
        }
        throw new NoBrandFoundException("No brand found for brandId: " + brandId);
    }
}
