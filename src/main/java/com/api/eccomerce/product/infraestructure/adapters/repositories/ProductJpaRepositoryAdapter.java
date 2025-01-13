package com.api.eccomerce.product.infraestructure.adapters.repositories;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.domain.ports.ProductPort;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductJpaRepositoryAdapter implements ProductPort {

    private final ProductRepository productRepository;
    private final PriceMapper priceMapper;

    @Autowired
    public ProductJpaRepositoryAdapter(
            ProductRepository productRepository, PriceMapper priceMapper) {
        this.productRepository = productRepository;
        this.priceMapper = priceMapper;
    }

    @Override
    public List<Price> retrieveProductPricesByDateTime(String productId, LocalDateTime dateTime) {
        return productRepository.findPricesByDateTimeAndProduct(productId, dateTime).stream()
                .map(priceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
