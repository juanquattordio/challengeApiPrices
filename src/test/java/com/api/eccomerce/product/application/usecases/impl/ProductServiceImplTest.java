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
    private static final String USD = "USD";
    private static final String T_SHIRT = "T-shirt";
    private static final LocalDateTime REQUEST_DATE_TIME =
            LocalDateTime.of(2024, 11, 15, 5, 15, 15);
    private static final Price PRICE_1 =
            new Price(1, 0, USD, 16.5, LocalDateTime.of(2024, 10, 15, 5, 15, 15));
    private static final Price PRICE_2 =
            new Price(2, 1, USD, 17.5, LocalDateTime.of(2023, 10, 15, 5, 15, 15));
    private static final Price PRICE_3 =
            new Price(3, 0, USD, 15.5, LocalDateTime.of(2022, 10, 15, 5, 15, 15));
    private static final Price PRICE_4 =
            new Price(4, 1, USD, 25.5, LocalDateTime.of(2024, 10, 15, 5, 15, 15));
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
        listPrices.add(PRICE_3);
        listPrices.add(PRICE_4);

        when(productPort.retrieveProductPriceByDateTime(TS1, REQUEST_DATE_TIME))
                .thenReturn(listPrices);

        Optional<Price> product1Price =
                testee.getPriceByProductAndDateTime(product1.getCodeId(), REQUEST_DATE_TIME);

        assertEquals(25.5, product1Price.get().getValue());
        assertEquals(1, product1Price.get().getPriority());
        assertEquals(4, product1Price.get().getPriceList());
    }

    @Test
    void getPriceByProductAndDateTimeReceivesAndReturnsOnePrice() {
        listPrices.add(PRICE_3);

        when(productPort.retrieveProductPriceByDateTime(TS1, REQUEST_DATE_TIME))
                .thenReturn(listPrices);

        Optional<Price> product1Price =
                testee.getPriceByProductAndDateTime(product1.getCodeId(), REQUEST_DATE_TIME);

        assertEquals(15.5, product1Price.get().getValue());
        assertEquals(0, product1Price.get().getPriority());
        assertEquals(3, product1Price.get().getPriceList());
    }

    @Test
    void getPriceByProductAndDateTimeNoReceivesPricesAndReturnsNoPrice() {
        when(productPort.retrieveProductPriceByDateTime(TS1, REQUEST_DATE_TIME))
                .thenReturn(new ArrayList<>());

        Optional<Price> product1Price =
                testee.getPriceByProductAndDateTime(product1.getCodeId(), REQUEST_DATE_TIME);

        assertTrue(product1Price.isEmpty());
    }
}
