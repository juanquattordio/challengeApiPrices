package com.api.eccomerce.product.application.usecases.impl;

import com.api.eccomerce.product.application.ports.ProductPort;
import com.api.eccomerce.product.application.usecases.ProductService;
import com.api.eccomerce.product.domain.models.Price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductPort productPort;

    @Autowired
    public ProductServiceImpl(ProductPort productPort) {
        this.productPort = productPort;
    }

    @Override
    public Optional<Price> getHighestPriorityPriceByBrandAndProductAndDateTime(
            String brandId, String productId, LocalDateTime dateTime) {
        return productPort.retrieveHighestPriorityProductPriceByBrandAndDateTime(brandId, productId, dateTime);
    }
}
