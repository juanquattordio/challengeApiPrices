package com.api.eccomerce.product.application.ports;

import com.api.eccomerce.product.domain.models.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductPort {
    Optional<Price> retrieveHighestPriorityProductPriceByBrandAndDateTime(
            String brandId, String productId, LocalDateTime dateTime);
}
