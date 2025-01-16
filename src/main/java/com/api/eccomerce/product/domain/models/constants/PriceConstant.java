package com.api.eccomerce.product.domain.models.constants;

public class PriceConstant {
    public static final String INCOMPATIBLE_START_END_TIME_MESSAGE_ERROR =
            "endDateTime must not be before startDateTime";
    public static final String NEGATIVE_PRICE_VALUE_MESSAGE_ERROR =
            "the price value must not be negative";

    private PriceConstant() {}
}
