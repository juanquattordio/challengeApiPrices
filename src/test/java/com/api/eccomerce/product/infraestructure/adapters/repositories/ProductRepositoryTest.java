package com.api.eccomerce.product.infraestructure.adapters.repositories;

import static com.api.eccomerce.product.domain.value_objetcs.Currency.EUR;

import static org.junit.jupiter.api.Assertions.*;

import com.api.eccomerce.product.infraestructure.adapters.repositories.entities.PriceEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
class ProductRepositoryTest {

    private static final String BRAND_1 = "brand1";
    private static final String PRODUCT_ID = "35455";
    private static final LocalDateTime DATE_TIME_OK = LocalDateTime.of(2020, 6, 14, 16, 0, 0);
    @Autowired private ProductRepository testee;
    @Autowired private TestEntityManager entityManager;

    @BeforeEach
    public void setup() {
        fillOutMockDataBase();
    }

    @Test
    @DisplayName("When many date times for a product match with condition, it returns many prices")
    void findHighestPriorityPriceByDateTimeAndProductReturnsSeveralPrices() {

        Optional<PriceEntity> price =
                testee.findHighestPriorityPriceByBrandAndProductAndDateTime(
                        BRAND_1, PRODUCT_ID, DATE_TIME_OK);

        assertEquals(1, price.get().getPriority());
        assertEquals(25.45, price.get().getPrice());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCasesWithEmptyReturns")
    @DisplayName(
            "When no date time and product and brand match with condition, it returns empty list prices")
    void findHighestPriorityPriceByDateTimeAndProductReturnsNoPrice(
            String testName, String brandId, String productId, LocalDateTime dateTime) {

        Optional<PriceEntity> price =
                testee.findHighestPriorityPriceByBrandAndProductAndDateTime(
                        brandId, productId, dateTime);

        assertEquals(Optional.empty(), price);
    }

    private void fillOutMockDataBase() {
        PriceEntity price1 =
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
        PriceEntity price2 =
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
        PriceEntity price3 =
                new PriceEntity(
                        null,
                        BRAND_1,
                        LocalDateTime.of(2020, 6, 15, 0, 0, 0),
                        LocalDateTime.of(2020, 6, 15, 11, 0, 0),
                        3,
                        PRODUCT_ID,
                        1,
                        30.50,
                        EUR.toString());
        PriceEntity price4 =
                new PriceEntity(
                        null,
                        BRAND_1,
                        LocalDateTime.of(2020, 6, 15, 16, 0, 0),
                        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                        4,
                        PRODUCT_ID,
                        1,
                        38.95,
                        EUR.toString());

        entityManager.persist(price1);
        entityManager.persist(price2);
        entityManager.persist(price3);
        entityManager.persist(price4);
    }

    private static Stream<Arguments> provideTestCasesWithEmptyReturns() {
        return Stream.of(
                Arguments.of("Product does not match", BRAND_1, "anotherProduct", DATE_TIME_OK),
                Arguments.of("Brand does not match", "anotherBrand", PRODUCT_ID, DATE_TIME_OK),
                Arguments.of(
                        "DateTime does not match",
                        BRAND_1,
                        PRODUCT_ID,
                        LocalDateTime.of(2019, 6, 14, 16, 0, 0)));
    }
}
