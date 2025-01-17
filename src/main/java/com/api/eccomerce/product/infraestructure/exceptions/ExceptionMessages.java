package com.api.eccomerce.product.infraestructure.exceptions;

public class ExceptionMessages {
    public static final String BRAND_ID_BLANK_EXCEPTION = "brandId must not be blank";
    public static final String PRODUCT_ID_BLANK_EXCEPTION = "productId must not be blank";
    public static final String INVALID_DATE_TIME_EXCEPTION =
            "Invalid dateTime format. Please use UTC format (e.g., 2025-01-16T23:08:59Z)";

    private ExceptionMessages() {}
}
