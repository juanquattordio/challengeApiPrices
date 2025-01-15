package com.api.eccomerce.product.infraestructure.adapters.repositories;

import com.api.eccomerce.product.application.ports.ProductPort;
import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<Price> retrieveProductPricesByBrandAndDateTime(
            String brandId, String productId, LocalDateTime dateTime) {
        return productRepository
                .findPricesByBrandAndProductAndDateTime(brandId, productId, dateTime)
                .stream()
                .map(priceMapper::toDomain)
                .toList();
    }
}
