package com.api.eccomerce.product.application.usecases.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.domain.models.Product;
import com.api.eccomerce.product.domain.ports.ProductPort;

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

    private static final String USD = "USD";
    @Mock private ProductPort productPort;
    @InjectMocks ProductServiceImpl testee;

    @Test
    void getPriceByProductAndDateTime() {

        Price price1 = new Price(1, 0, USD, 16.5, LocalDateTime.of(2024, 10, 15, 5, 15, 15));
        Price price2 = new Price(1, 1, USD, 17.5, LocalDateTime.of(2023, 10, 15, 5, 15, 15));
        Price price3 = new Price(1, 1, USD, 15.5, LocalDateTime.of(2022, 10, 15, 5, 15, 15));
        Price price4 = new Price(1, 1, USD, 25.5, LocalDateTime.of(2024, 10, 15, 5, 15, 15));

        List<Price> listPrices = new ArrayList<>();
        listPrices.add(price1);
        listPrices.add(price2);
        listPrices.add(price3);
        listPrices.add(price4);

        LocalDateTime requestDateTime = LocalDateTime.of(2024, 11, 15, 5, 15, 15);

        Product product1 =
                new Product("1TS", "T'shirt", List.of(price1, price2, price3, price4), "2");

        when(productPort.retrieveProductPriceByDateTime("1TS", requestDateTime))
                .thenReturn(listPrices);

        Optional<Price> product1Price =
                testee.getPriceByProductAndDateTime(product1.getCodeId(), requestDateTime);

        assertEquals(25.5, product1Price.get().getValue());
    }
}
