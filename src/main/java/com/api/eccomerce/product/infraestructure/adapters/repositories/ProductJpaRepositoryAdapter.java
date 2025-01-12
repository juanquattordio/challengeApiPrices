package com.api.eccomerce.product.infraestructure.adapters.repositories;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.domain.ports.ProductPort;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class ProductJpaRepositoryAdapter implements ProductPort {

    @Override
    public List<Price> retrieveProductPriceByDateTime(String productId, LocalDateTime dateTime) {
        return Collections.emptyList();
    }
}
