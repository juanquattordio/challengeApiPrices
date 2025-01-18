package com.api.eccomerce.product.application.usecases.impl;

import com.api.eccomerce.product.application.ports.ProductPort;
import com.api.eccomerce.product.application.usecases.ProductService;
import com.api.eccomerce.product.domain.models.Price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductPort productPort;

    @Autowired
    public ProductServiceImpl(ProductPort productPort) {
        this.productPort = productPort;
    }

    @Override
    public Optional<Price> getPriceByBrandAndProductAndDateTime(
            String brandId, String productId, LocalDateTime dateTime) {
        List<Price> prices =
                productPort.retrieveProductPricesByBrandAndDateTime(brandId, productId, dateTime);
        return choosePriceByPriority(prices);
    }

    private static Optional<Price> choosePriceByPriority(List<Price> prices) {
        if (prices.size() == 1) {
            return Optional.ofNullable(prices.get(0));
        }

        return prices.stream().max(Comparator.comparing(Price::getPriority));
    }
}
