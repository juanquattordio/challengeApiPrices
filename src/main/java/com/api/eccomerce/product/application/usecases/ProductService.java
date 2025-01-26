package com.api.eccomerce.product.application.usecases;

import com.api.eccomerce.product.domain.models.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ProductService {
    Optional<Price> getHighestPriorityPriceByBrandAndProductAndDateTime(
            String brandId, String productId, LocalDateTime dateTime);
}
