package com.api.eccomerce.product.infraestructure.adapters.repositories;

import static com.api.eccomerce.product.domain.value_objetcs.Currency.EUR;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;
import com.api.eccomerce.product.infraestructure.adapters.repositories.entities.PriceEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductJpaRepositoryAdapterTest {
    private static final String BRAND_1 = "brand1";
    private static final String PRODUCT_ID = "35455";
    private static final LocalDateTime DATE_TIME_OK = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
    private static final PriceEntity PRICE_ENTITY =
            new PriceEntity(
                    null,
                    BRAND_1,
                    LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                    LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                    1,
                    PRODUCT_ID,
                    0,
                    35.5,
                    EUR.toString());
    private static final Price PRICE_DOMAIN =
            new Price(
                    1,
                    EUR.toString(),
                    35.5,
                    LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                    LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    @InjectMocks private ProductJpaRepositoryAdapter testee;
    @Mock private ProductRepository productRepository;
    @Mock private PriceMapper priceMapper;

    @Test
    void retrieveProductHighestPriorityPriceByBrandAndDateTimeReturnsOnlyOnePrice() {
        when(productRepository.findHighestPriorityPriceByBrandAndProductAndDateTime(
                        BRAND_1, PRODUCT_ID, DATE_TIME_OK))
                .thenReturn(Optional.of(PRICE_ENTITY));
        when(priceMapper.toDomain(PRICE_ENTITY)).thenReturn(PRICE_DOMAIN);

        Optional<Price> price =
                testee.retrieveHighestPriorityProductPriceByBrandAndDateTime(
                        BRAND_1, PRODUCT_ID, DATE_TIME_OK);

        assertEquals(35.5, price.get().getValue());
    }

    @Test
    void retrieveHighestPriorityProductPriceByBrandAndDateTimeReturnsNoPrice() {
        when(productRepository.findHighestPriorityPriceByBrandAndProductAndDateTime(
                        BRAND_1, PRODUCT_ID, DATE_TIME_OK))
                .thenReturn(Optional.of(PRICE_ENTITY));

        Optional<Price> price =
                testee.retrieveHighestPriorityProductPriceByBrandAndDateTime(
                        BRAND_1, PRODUCT_ID, DATE_TIME_OK);

        assertEquals(Optional.empty(), price);
    }
}
