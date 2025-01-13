package com.api.eccomerce.product.application.usecases.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.domain.models.Product;
import com.api.eccomerce.product.domain.ports.ProductPort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ProductServiceImplTest {

    private static final String TS1 = "1TS";
    private static final String EUR = "EUR";
    private static final String T_SHIRT = "T-shirt";
    private static final Price PRICE_1 =
            new Price(
                    1,
                    0,
                    EUR,
                    35.5,
                    LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                    LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    private static final Price PRICE_2 =
            new Price(
                    2,
                    1,
                    EUR,
                    25.45,
                    LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                    LocalDateTime.of(2020, 6, 14, 18, 30, 0));
    private static final Price PRICE_3 =
            new Price(
                    3,
                    1,
                    EUR,
                    30.50,
                    LocalDateTime.of(2020, 6, 15, 0, 0, 0),
                    LocalDateTime.of(2020, 6, 15, 11, 0, 0));
    private static final Price PRICE_4 =
            new Price(
                    4,
                    1,
                    EUR,
                    38.95,
                    LocalDateTime.of(2020, 6, 15, 16, 0, 0),
                    LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    private List<Price> listPrices;
    private Product product1;

    @InjectMocks private ProductServiceImpl testee;
    @Mock private ProductPort productPort;

    @BeforeEach
    public void setup() {
        listPrices = new ArrayList<>();
        product1 =
                Product.builder()
                        .codeId(TS1)
                        .description(T_SHIRT)
                        .prices(listPrices)
                        .brandId("2")
                        .build();
    }

    @Test
    void getPriceByProductAndDateTimeReceivesMoreThanOnePriceAndChoosesByMaxPriority() {
        listPrices.add(PRICE_1);
        listPrices.add(PRICE_2);
        LocalDateTime requestDateTime =
                LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        when(productPort.retrieveProductPricesByDateTime(TS1, requestDateTime))
                .thenReturn(listPrices);

        Optional<Price> product1Price =
                testee.getPriceByProductAndDateTime(product1.getCodeId(), requestDateTime);

        assertEquals(25.45, product1Price.get().getValue());
        assertEquals(1, product1Price.get().getPriority());
        assertEquals(2, product1Price.get().getPriceList());
    }

    @Test
    void getPriceByProductAndDateTimeReceivesOnePriceAndReturns() {
        listPrices.add(PRICE_1);
        LocalDateTime requestDateTime =
                LocalDateTime.of(2020, 6, 14, 21, 0, 0);

        when(productPort.retrieveProductPricesByDateTime(TS1, requestDateTime))
                .thenReturn(listPrices);

        Optional<Price> product1Price =
                testee.getPriceByProductAndDateTime(product1.getCodeId(), requestDateTime);

        assertEquals(35.5, product1Price.get().getValue());
        assertEquals(0, product1Price.get().getPriority());
        assertEquals(1, product1Price.get().getPriceList());
    }

    @Test
    void getPriceByProductAndDateTimeNoReceivesPricesAndReturnsNoPrice() {
        LocalDateTime requestDateTime =
                LocalDateTime.of(2019, 6, 14, 21, 0, 0);

        when(productPort.retrieveProductPricesByDateTime(TS1, requestDateTime))
                .thenReturn(new ArrayList<>());

        Optional<Price> product1Price =
                testee.getPriceByProductAndDateTime(product1.getCodeId(), requestDateTime);

        assertTrue(product1Price.isEmpty());
    }
}
