package com.api.eccomerce.product.application.usecases.impl;

import static com.api.eccomerce.product.domain.value_objetcs.Currency.EUR;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.api.eccomerce.product.application.ports.ProductPort;
import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.domain.models.Product;
import com.api.eccomerce.product.domain.value_objetcs.Brand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    private static final String TS1 = "35455";
    private static final String T_SHIRT = "T-shirt";
    private static final Price PRICE_1 =
            new Price(
                    1,
                    EUR.toString(),
                    35.5,
                    LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                    LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    private Product product1;

    @InjectMocks private ProductServiceImpl testee;
    @Mock private ProductPort productPort;

    @BeforeEach
    public void setup() {
        product1 =
                Product.builder()
                        .codeId(TS1)
                        .description(T_SHIRT)
                        .prices(new ArrayList<>())
                        .brand(Brand.ZARA)
                        .build();
    }

    @Test
    @DisplayName(
            "When getPriceByProductAndDateTime receives few prices, it should return the max prioritized")
    void getHighestPriorityPriceByProductAndDateTimeReceivesAndReturnsOnlyOnePrice() {
        LocalDateTime requestDateTime = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        when(productPort.retrieveHighestPriorityProductPriceByBrandAndDateTime(
                        Brand.ZARA.getId(), TS1, requestDateTime))
                .thenReturn(Optional.of(PRICE_1));

        Optional<Price> product1Price =
                testee.getHighestPriorityPriceByBrandAndProductAndDateTime(
                        product1.getBrand().getId(), product1.getCodeId(), requestDateTime);

        assertEquals(35.5, product1Price.get().getValue());
        assertEquals(1, product1Price.get().getPriceList());
    }

    @Test
    @DisplayName(
            "When getPriceByProductAndDateTime receives empty list of prices, it should return that empty list")
    void getPriceByProductAndDateTimeNoReceivesPricesAndReturnsNoPrice() {
        LocalDateTime requestDateTime = LocalDateTime.of(2019, 6, 14, 21, 0, 0);

        when(productPort.retrieveHighestPriorityProductPriceByBrandAndDateTime(
                        Brand.ZARA.getId(), TS1, requestDateTime))
                .thenReturn(Optional.empty());

        Optional<Price> product1Price =
                testee.getHighestPriorityPriceByBrandAndProductAndDateTime(
                        product1.getBrand().getId(), product1.getCodeId(), requestDateTime);

        assertTrue(product1Price.isEmpty());
    }
}
