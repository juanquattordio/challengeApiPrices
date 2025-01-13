package com.api.eccomerce.product.domain.ports;

import com.api.eccomerce.product.domain.models.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductPort {
    List<Price> retrieveProductPricesByDateTime(String productId, LocalDateTime dateTime);
}
