package com.api.eccomerce.product.infraestructure.adapters.repositories;

import static com.api.eccomerce.product.domain.valueObjetcs.Currency.EUR;

import static org.junit.jupiter.api.Assertions.*;

import com.api.eccomerce.product.infraestructure.adapters.repositories.entities.PriceEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = "spring.sql.init.mode=never")
class ProductRepositoryTest {

    @Autowired private ProductRepository testee;
    @Autowired private TestEntityManager entityManager;

    @BeforeEach
    public void setup() {
        fillOutMockDataBase();
    }

    @Test
    @DisplayName("When many date times for a product match with condition, it returns many prices")
    void findPricesByDateTimeAndProductReturnsSeveralPrices() {

        List<PriceEntity> priceList =
                testee.findPricesByDateTimeAndProduct(
                        "35455", LocalDateTime.of(2020, 6, 14, 16, 0, 0));

        assertEquals(2, priceList.size());
    }

    private void fillOutMockDataBase() {
        PriceEntity price1 =
                new PriceEntity(
                        null,
                        "brand1",
                        LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                        1,
                        "35455",
                        0,
                        35.5,
                        EUR.toString());
        PriceEntity price2 =
                new PriceEntity(
                        null,
                        "brand1",
                        LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                        LocalDateTime.of(2020, 6, 14, 18, 30, 0),
                        2,
                        "35455",
                        1,
                        25.45,
                        EUR.toString());
        PriceEntity price3 =
                new PriceEntity(
                        null,
                        "brand1",
                        LocalDateTime.of(2020, 6, 15, 0, 0, 0),
                        LocalDateTime.of(2020, 6, 15, 11, 0, 0),
                        3,
                        "35455",
                        1,
                        30.50,
                        EUR.toString());
        PriceEntity price4 =
                new PriceEntity(
                        null,
                        "brand1",
                        LocalDateTime.of(2020, 6, 15, 16, 0, 0),
                        LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                        4,
                        "35455",
                        1,
                        38.95,
                        EUR.toString());

        entityManager.persist(price1);
        entityManager.persist(price2);
        entityManager.persist(price3);
        entityManager.persist(price4);
    }
}
