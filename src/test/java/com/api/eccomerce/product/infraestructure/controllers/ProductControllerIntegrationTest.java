package com.api.eccomerce.product.infraestructure.controllers;

import static com.api.eccomerce.product.domain.value_objetcs.Currency.EUR;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;
import com.api.eccomerce.product.infraestructure.controllers.responses.BrandResponse;
import com.api.eccomerce.product.infraestructure.controllers.responses.PriceResponse;
import com.api.eccomerce.product.infraestructure.controllers.responses.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Stream;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {
    private static final String BRAND_ID = "1";
    private static final String PRODUCT_ID = "35455";
    private static final String GET_PRICE_PATH = "/products/%s/%s/prices";
    private static final String DATE_TIME_UTC_PARAM = "dateTimeUTC";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static List<PriceResponse> priceListResponse;
    @Autowired private MockMvc mockMvc;
    @Autowired private PriceMapper mapper;

    @BeforeEach
    void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        objectMapper.registerModule(new JavaTimeModule());
        priceListResponse = new ArrayList<>();
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void getPriceByBrandAndProductAndDateTimeShouldReturnsOnePrice(
            String brandId, String productId, String dateTime, Price expectedPrice)
            throws Exception {
        PriceResponse priceResponse = mapper.toResponseDTO(expectedPrice);
        if (priceResponse != null) {
            priceListResponse.add(priceResponse);
        }

        ProductResponse expectedResponse =
                ProductResponse.builder()
                        .brand(new BrandResponse(brandId))
                        .productCodeId(productId)
                        .prices(priceListResponse)
                        .build();

        String apiResponse =
                mockMvc.perform(
                                get(String.format(GET_PRICE_PATH, brandId, productId))
                                        .param(DATE_TIME_UTC_PARAM, dateTime))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        ProductResponse actualResponse = objectMapper.readValue(apiResponse, ProductResponse.class);

        assertEquals(expectedResponse, actualResponse);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(
                        BRAND_ID,
                        PRODUCT_ID,
                        "2020-06-14T10:00:00Z",
                        new Price(
                                1,
                                EUR.toString(),
                                35.5,
                                LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59, 59))),
                Arguments.of(
                        BRAND_ID,
                        PRODUCT_ID,
                        "2020-06-14T16:00:00Z",
                        new Price(
                                2,
                                EUR.toString(),
                                25.45,
                                LocalDateTime.of(2020, 6, 14, 15, 0, 0),
                                LocalDateTime.of(2020, 6, 14, 18, 30, 0))),
                Arguments.of(
                        BRAND_ID,
                        PRODUCT_ID,
                        "2020-06-14T21:00:00Z",
                        new Price(
                                1,
                                EUR.toString(),
                                35.5,
                                LocalDateTime.of(2020, 6, 14, 0, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59, 59))),
                Arguments.of(
                        BRAND_ID,
                        PRODUCT_ID,
                        "2020-06-15T10:00:00Z",
                        new Price(
                                3,
                                EUR.toString(),
                                30.50,
                                LocalDateTime.of(2020, 6, 15, 0, 0, 0),
                                LocalDateTime.of(2020, 6, 15, 11, 0, 0))),
                Arguments.of(
                        BRAND_ID,
                        PRODUCT_ID,
                        "2020-06-16T21:00:00Z",
                        new Price(
                                4,
                                EUR.toString(),
                                38.95,
                                LocalDateTime.of(2020, 6, 15, 16, 0, 0),
                                LocalDateTime.of(2020, 12, 31, 23, 59, 59))),
                Arguments.of(
                        "2",
                        "12345",
                        null,
                        new Price(
                                0,
                                EUR.toString(),
                                8.95,
                                LocalDateTime.of(2025, 1, 1, 0, 0, 0),
                                LocalDateTime.of(2028, 1, 1, 23, 59, 59))),
                Arguments.of("3", "12345", null, null));
    }
}
