package com.api.eccomerce.product.infraestructure.adapters.repositories;

import static com.api.eccomerce.product.domain.value_objetcs.Currency.EUR;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;
import com.api.eccomerce.product.infraestructure.adapters.repositories.entities.PriceEntity;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ProductJpaRepositoryAdapterTest {
    private static final String BRAND_1 = "brand1";
    private static final String PRODUCT_ID = "35455";
    private static final LocalDateTime DATE_TIME_OK = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
    private static final PriceEntity PRICE_1 =
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
    private static final PriceEntity PRICE_2 =
            new PriceEntity(
                    null,
                    BRAND_1,
                    LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                    LocalDateTime.of(2020, 6, 14, 18, 30, 0),
                    2,
                    PRODUCT_ID,
                    1,
                    25.45,
                    EUR.toString());
    private List<PriceEntity> priceListMock;
    @InjectMocks private ProductJpaRepositoryAdapter testee;
    @Mock private ProductRepository productRepository;
    @Spy private PriceMapper priceMapper;

    @Test
    void retrieveProductPricesByBrandAndDateTimeRetrievePriceList() {
        priceListMock = new ArrayList<>();
        priceListMock.add(PRICE_1);
        priceListMock.add(PRICE_2);

        when(productRepository.findPricesByBrandAndProductAndDateTime(
                        BRAND_1, PRODUCT_ID, DATE_TIME_OK))
                .thenReturn(priceListMock);

        List<Price> priceList =
                testee.retrieveProductPricesByBrandAndDateTime(BRAND_1, PRODUCT_ID, DATE_TIME_OK);

        assertEquals(2, priceList.size());
    }

    @Test
    void retrieveProductPricesByBrandAndDateTimeReturnsEmptyList() {
        priceListMock = new ArrayList<>();

        when(productRepository.findPricesByBrandAndProductAndDateTime(
                        BRAND_1, PRODUCT_ID, DATE_TIME_OK))
                .thenReturn(priceListMock);

        List<Price> priceList =
                testee.retrieveProductPricesByBrandAndDateTime(BRAND_1, PRODUCT_ID, DATE_TIME_OK);

        assertEquals(0, priceList.size());
    }
}
