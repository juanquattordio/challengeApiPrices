package com.api.eccomerce.product.infraestructure.controllers;

import static com.api.eccomerce.product.domain.value_objetcs.Currency.EUR;
import static com.api.eccomerce.product.infraestructure.exceptions.ExceptionMessages.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.api.eccomerce.product.application.usecases.ProductService;
import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;
import com.api.eccomerce.product.infraestructure.controllers.responses.BrandResponse;
import com.api.eccomerce.product.infraestructure.controllers.responses.PriceResponse;
import com.api.eccomerce.product.infraestructure.controllers.responses.ProductResponse;
import com.api.eccomerce.product.infraestructure.exceptions.EmptyInputException;
import com.api.eccomerce.product.infraestructure.exceptions.InvalidDateTimeFormatException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    private static final String BRAND_ID = "1";
    private static final String PRODUCT_ID = "35455";
    private static final String DATE_TIME_REQUEST = "2020-06-14T00:00:00Z";
    private static final LocalDateTime DATE_TIME_UTC = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
    private static final Price PRICE_COMPLETE =
            new Price(
                    1,
                    0,
                    EUR.toString(),
                    35.5,
                    DATE_TIME_UTC,
                    LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    private static final String GET_PRICE_PATH = "/products/%s/%s/prices";
    private static final String DATE_TIME_UTC_PARAM = "dateTimeUTC";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired private MockMvc mockMvc;
    @MockitoBean private ProductService serviceMock;
    @MockitoBean private PriceMapper mapperMock;

    @BeforeAll
    static void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getPriceByBrandAndProductAndDateTimeShouldReturnsOnePrice() throws Exception {
        PriceResponse priceResponse = toPriceResponse(PRICE_COMPLETE);
        List<PriceResponse> priceListResponse = new ArrayList<>();
        priceListResponse.add(priceResponse);
        ProductResponse expectedResponse =
                ProductResponse.builder()
                        .brand(new BrandResponse(BRAND_ID))
                        .productCodeId(PRODUCT_ID)
                        .prices(priceListResponse)
                        .build();

        when(serviceMock.getPriceByBrandAndProductAndDateTime(BRAND_ID, PRODUCT_ID, DATE_TIME_UTC))
                .thenReturn(Optional.of(PRICE_COMPLETE));
        when(mapperMock.toResponseDTO(PRICE_COMPLETE)).thenReturn(priceResponse);

        String apiResponse =
                mockMvc.perform(
                                get(String.format(GET_PRICE_PATH, BRAND_ID, PRODUCT_ID))
                                        .param(DATE_TIME_UTC_PARAM, DATE_TIME_REQUEST))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        ProductResponse actualResponse = objectMapper.readValue(apiResponse, ProductResponse.class);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("When no dateTimeUTC param is like dateTimeUTC equals to now")
    void whenNoDateTimeUTCParamProcessDateTimeParamShouldReturnNow() throws Exception {
        LocalDateTime dateTimeNow = LocalDateTime.of(2022, 1, 1, 12, 0);
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime
                    .when(() -> LocalDateTime.now(ZoneId.of("UTC")))
                    .thenReturn(dateTimeNow);

            PriceResponse priceResponse = toPriceResponse(PRICE_COMPLETE);

            when(serviceMock.getPriceByBrandAndProductAndDateTime(
                            BRAND_ID, PRODUCT_ID, dateTimeNow))
                    .thenReturn(Optional.of(PRICE_COMPLETE));
            when(mapperMock.toResponseDTO(PRICE_COMPLETE)).thenReturn(priceResponse);

            mockMvc.perform(get(String.format(GET_PRICE_PATH, BRAND_ID, PRODUCT_ID)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            verify(serviceMock, times(1))
                    .getPriceByBrandAndProductAndDateTime(BRAND_ID, PRODUCT_ID, dateTimeNow);
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCasesWithWrongInputs")
    @DisplayName(
            "When brandId or productId or datetime input is not valid, it should returns Bad Request error")
    void whenInvalidDateTimeParamShouldReturnsHttp400(
            String testName,
            String brandId,
            String productId,
            String dateTime,
            String exceptionType,
            String expectedMessage)
            throws Exception {
        mockMvc.perform(
                        get(String.format(GET_PRICE_PATH, brandId, productId))
                                .param(DATE_TIME_UTC_PARAM, dateTime))
                .andExpect(status().isBadRequest())
                .andExpect(
                        result ->
                                assertEquals(
                                        result.getResolvedException().getClass().toString(),
                                        exceptionType))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    private PriceResponse toPriceResponse(Price priceDomain) {
        return new PriceResponse(
                priceDomain.getPriceList(),
                priceDomain.getPriority(),
                priceDomain.getCurrency(),
                priceDomain.getValue(),
                priceDomain.getStartDateTime(),
                priceDomain.getEndDateTime());
    }

    private static Stream<Arguments> provideTestCasesWithWrongInputs() {
        return Stream.of(
                Arguments.of(
                        "BrandId is blank",
                        " ",
                        PRODUCT_ID,
                        DATE_TIME_UTC_PARAM,
                        EmptyInputException.class.toString(),
                        BRAND_ID_BLANK_EXCEPTION),
                Arguments.of(
                        "ProductId is blank",
                        BRAND_ID,
                        " ",
                        DATE_TIME_UTC_PARAM,
                        EmptyInputException.class.toString(),
                        PRODUCT_ID_BLANK_EXCEPTION),
                Arguments.of(
                        "DateTime invalid format",
                        BRAND_ID,
                        PRODUCT_ID,
                        "16/01/2025 23:08:00",
                        InvalidDateTimeFormatException.class.toString(),
                        INVALID_DATE_TIME_EXCEPTION),
                Arguments.of(
                        "DateTime different time zone (negative)",
                        BRAND_ID,
                        PRODUCT_ID,
                        "2025-01-16T23:08:00-01:00",
                        InvalidDateTimeFormatException.class.toString(),
                        INVALID_DATE_TIME_EXCEPTION),
                Arguments.of(
                        "DateTime different time zone (positive)",
                        BRAND_ID,
                        PRODUCT_ID,
                        "2025-01-16T23:08:00+01:00",
                        InvalidDateTimeFormatException.class.toString(),
                        INVALID_DATE_TIME_EXCEPTION),
                Arguments.of(
                        "DateTime without time zone",
                        BRAND_ID,
                        PRODUCT_ID,
                        "2025-01-16T23:08:00",
                        InvalidDateTimeFormatException.class.toString(),
                        INVALID_DATE_TIME_EXCEPTION));
    }
}
