package com.api.eccomerce.product.domain.models;

import static com.api.eccomerce.product.domain.models.constants.PriceConstant.INCOMPATIBLE_START_END_TIME_MESSAGE_ERROR;
import static com.api.eccomerce.product.domain.models.constants.PriceConstant.NEGATIVE_PRICE_VALUE_MESSAGE_ERROR;

import static org.junit.jupiter.api.Assertions.*;

import com.api.eccomerce.product.domain.models.exceptions.PriceException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class PriceTest {

    private final LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
    private final LocalDateTime endDate = startDate.plusDays(1);

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        assertThrowsExactly(
                PriceException.class,
                () -> Price.builder().value(-10D).build(),
                NEGATIVE_PRICE_VALUE_MESSAGE_ERROR);
    }

    @Test
    void shouldThrowExceptionWhenStartDateAfterEndDate() {
        assertThrowsExactly(
                PriceException.class,
                () ->
                        Price.builder()
                                .startDateTime(startDate)
                                .endDateTime(startDate.minusDays(1))
                                .build(),
                INCOMPATIBLE_START_END_TIME_MESSAGE_ERROR);
    }

    @Test
    void shouldCreatePriceWhenValidInputs() {
        Price price =
                Price.builder().value(10D).startDateTime(startDate).endDateTime(endDate).build();

        assertNotNull(price);
        assertEquals(10D, price.getValue());
        assertEquals(startDate, price.getStartDateTime());
        assertEquals(endDate, price.getEndDateTime());
    }
}
