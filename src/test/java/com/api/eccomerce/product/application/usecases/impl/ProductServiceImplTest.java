package com.api.eccomerce.product.application.usecases.impl;

import static com.api.eccomerce.product.domain.valueObjetcs.Currency.EUR;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.domain.models.Product;
import com.api.eccomerce.product.domain.ports.ProductPort;
import com.api.eccomerce.product.domain.valueObjetcs.Brand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    private static final String TS1 = "35455";
    private static final String T_SHIRT = "T-shirt";
    private static final Price PRICE_1 =
            new Price(
                    1,
                    0,
                    EUR.toString(),
                    35.5,
                    LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                    LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    private static final Price PRICE_2 =
            new Price(
                    2,
                    1,
                    EUR.toString(),
                    25.45,
                    LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                    LocalDateTime.of(2020, 6, 14, 18, 30, 0));
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
                        .brand(Brand.ZARA)
                        .build();
    }

    @Test
    @DisplayName(
            "When getPriceByProductAndDateTime receives few prices, it should return the max prioritized")
    void getPriceByProductAndDateTimeReceivesMoreThanOnePriceAndChoosesByMaxPriority() {
        listPrices.add(PRICE_1);
        listPrices.add(PRICE_2);
        LocalDateTime requestDateTime = LocalDateTime.of(2020, 6, 14, 16, 0, 0);

        when(productPort.retrieveProductPricesByBrandAndDateTime(
                        Brand.ZARA.getId(), TS1, requestDateTime))
                .thenReturn(listPrices);

        Optional<Price> product1Price =
                testee.getPriceByBrandAndProductAndDateTime(
                        product1.getBrand().getId(), product1.getCodeId(), requestDateTime);

        assertEquals(25.45, product1Price.get().getValue());
        assertEquals(1, product1Price.get().getPriority());
        assertEquals(2, product1Price.get().getPriceList());
    }

    @Test
    @DisplayName(
            "When getPriceByProductAndDateTime receives only one price, it should return that one")
    void getPriceByProductAndDateTimeReceivesOnePriceAndReturns() {
        listPrices.add(PRICE_1);
        LocalDateTime requestDateTime = LocalDateTime.of(2020, 6, 14, 21, 0, 0);

        when(productPort.retrieveProductPricesByBrandAndDateTime(
                        Brand.ZARA.getId(), TS1, requestDateTime))
                .thenReturn(listPrices);

        Optional<Price> product1Price =
                testee.getPriceByBrandAndProductAndDateTime(
                        product1.getBrand().getId(), product1.getCodeId(), requestDateTime);

        assertEquals(35.5, product1Price.get().getValue());
        assertEquals(0, product1Price.get().getPriority());
        assertEquals(1, product1Price.get().getPriceList());
    }

    @Test
    @DisplayName(
            "When getPriceByProductAndDateTime receives empty list of prices, it should return that empty list")
    void getPriceByProductAndDateTimeNoReceivesPricesAndReturnsNoPrice() {
        LocalDateTime requestDateTime = LocalDateTime.of(2019, 6, 14, 21, 0, 0);

        when(productPort.retrieveProductPricesByBrandAndDateTime(
                        Brand.ZARA.getId(), TS1, requestDateTime))
                .thenReturn(new ArrayList<>());

        Optional<Price> product1Price =
                testee.getPriceByBrandAndProductAndDateTime(
                        product1.getBrand().getId(), product1.getCodeId(), requestDateTime);

        assertTrue(product1Price.isEmpty());
    }
}
