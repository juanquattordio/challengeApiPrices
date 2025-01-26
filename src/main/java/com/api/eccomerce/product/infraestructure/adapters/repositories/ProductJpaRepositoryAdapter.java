package com.api.eccomerce.product.infraestructure.adapters.repositories;

import com.api.eccomerce.product.application.ports.ProductPort;
import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;

import com.api.eccomerce.product.infraestructure.adapters.repositories.entities.PriceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public Optional<Price> retrieveHighestPriorityProductPriceByBrandAndDateTime(
            String brandId, String productId, LocalDateTime dateTime) {
        Optional<PriceEntity> priceEntity =productRepository
                .findHighestPriorityPriceByBrandAndProductAndDateTime(brandId, productId, dateTime);
        return priceEntity.map(priceMapper::toDomain);
    }
}
