package com.api.eccomerce.product.infraestructure.adapters.repositories;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.domain.ports.ProductPort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class ProductJpaRepositoryAdapter implements ProductPort {

    private final ProductRepository productRepository;

    @Autowired
    public ProductJpaRepositoryAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Price> retrieveProductPricesByDateTime(String productId, LocalDateTime dateTime) {
        return Collections.emptyList();
    }
}
