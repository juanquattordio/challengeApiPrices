package com.api.eccomerce.product.infraestructure.controllers;

import static com.api.eccomerce.product.domain.models.constants.PriceConstant.NEGATIVE_PRICE_VALUE_MESSAGE_ERROR;
import static com.api.eccomerce.product.domain.value_objetcs.Currency.EUR;
import static com.api.eccomerce.product.infraestructure.exceptions.ExceptionMessages.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.api.eccomerce.product.application.usecases.ProductService;
import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.domain.models.exceptions.PriceException;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;
import com.api.eccomerce.product.infraestructure.controllers.responses.BrandResponse;
import com.api.eccomerce.product.infraestructure.controllers.responses.PriceResponse;
import com.api.eccomerce.product.infraestructure.controllers.responses.ProductResponse;
import com.api.eccomerce.product.infraestructure.exceptions.EmptyInputException;
import com.api.eccomerce.product.infraestructure.exceptions.InvalidDateTimeFormatException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    private static final String BRAND_ID = "1";
    private static final String PRODUCT_ID = "35455";
    private static final String DATE_TIME_REQUEST = "2020-06-14T00:00:00Z";
    private static final LocalDateTime DATE_TIME_UTC = LocalDateTime.of(2020, 6, 14, 0, 0, 0);
    private static final Price PRICE_COMPLETE =
            new Price(
                    1,
                    EUR.toString(),
                    35.5,
                    DATE_TIME_UTC,
                    LocalDateTime.of(2020, 12, 31, 23, 59, 59));
    private static final String DATE_TIME_UTC_PARAM = "dateTimeUTC";
    @InjectMocks private ProductController testee;
    @Mock private ProductService serviceMock;
    @Mock private PriceMapper mapperMock;

    @Test
    void getPriceByBrandAndProductAndDateTimeShouldReturnsOnePrice() {
        PriceResponse priceResponse = toPriceResponse(PRICE_COMPLETE);
        List<PriceResponse> priceListResponse = new ArrayList<>();
        priceListResponse.add(priceResponse);
        ProductResponse expectedResponse =
                ProductResponse.builder()
                        .brand(new BrandResponse(BRAND_ID))
                        .productCodeId(PRODUCT_ID)
                        .prices(priceListResponse)
                        .build();

        when(serviceMock.getHighestPriorityPriceByBrandAndProductAndDateTime(
                        BRAND_ID, PRODUCT_ID, DATE_TIME_UTC))
                .thenReturn(Optional.of(PRICE_COMPLETE));
        when(mapperMock.toResponseDTO(PRICE_COMPLETE)).thenReturn(priceResponse);

        ProductResponse apiResponse =
                testee.getPriceByBrandAndProductAndDateTime(
                        BRAND_ID, PRODUCT_ID, DATE_TIME_REQUEST);

        assertEquals(expectedResponse, apiResponse);
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesDateTimeParamNullOrBlank")
    @DisplayName("When no dateTimeUTC param is like dateTimeUTC equals to now")
    void whenNoDateTimeUTCParamProcessDateTimeParamShouldReturnNow(String dateTimeParam) {
        LocalDateTime dateTimeNow = LocalDateTime.of(2022, 1, 1, 12, 0);
        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime
                    .when(() -> LocalDateTime.now(ZoneId.of("UTC")))
                    .thenReturn(dateTimeNow);

            PriceResponse priceResponse = toPriceResponse(PRICE_COMPLETE);

            when(serviceMock.getHighestPriorityPriceByBrandAndProductAndDateTime(
                            BRAND_ID, PRODUCT_ID, dateTimeNow))
                    .thenReturn(Optional.of(PRICE_COMPLETE));
            when(mapperMock.toResponseDTO(PRICE_COMPLETE)).thenReturn(priceResponse);

            testee.getPriceByBrandAndProductAndDateTime(BRAND_ID, PRODUCT_ID, dateTimeParam);

            verify(serviceMock, times(1))
                    .getHighestPriorityPriceByBrandAndProductAndDateTime(
                            BRAND_ID, PRODUCT_ID, dateTimeNow);
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
            String expectedMessage) {

        Exception exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                testee.getPriceByBrandAndProductAndDateTime(
                                        brandId, productId, dateTime));
        assertEquals(exception.getClass().toString(), exceptionType);
        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    @DisplayName("When unexpected error occurs, it should return Internal Server error")
    void whenUnexpectedErrorShouldReturnInternalServerError() {
        when(serviceMock.getHighestPriorityPriceByBrandAndProductAndDateTime(
                        BRAND_ID, PRODUCT_ID, DATE_TIME_UTC))
                .thenThrow(HttpServerErrorException.InternalServerError.class);

        Exception exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                testee.getPriceByBrandAndProductAndDateTime(
                                        BRAND_ID, PRODUCT_ID, DATE_TIME_REQUEST));
        assertEquals(HttpServerErrorException.InternalServerError.class, exception.getClass());
    }

    @Test
    @DisplayName(
            "When an error occurs creating a Price, it should return Unprocessable Entity error")
    void whenModelPriceErrorShouldReturnPriceError() {
        when(serviceMock.getHighestPriorityPriceByBrandAndProductAndDateTime(
                        BRAND_ID, PRODUCT_ID, DATE_TIME_UTC))
                .thenThrow(
                        new PriceException(
                                NEGATIVE_PRICE_VALUE_MESSAGE_ERROR,
                                HttpStatus.UNPROCESSABLE_ENTITY));

        Exception exception =
                assertThrows(
                        RuntimeException.class,
                        () ->
                                testee.getPriceByBrandAndProductAndDateTime(
                                        BRAND_ID, PRODUCT_ID, DATE_TIME_REQUEST));

        assertEquals(PriceException.class, exception.getClass());
        assertEquals(NEGATIVE_PRICE_VALUE_MESSAGE_ERROR, exception.getMessage());
    }

    private PriceResponse toPriceResponse(Price priceDomain) {
        return new PriceResponse(
                priceDomain.getPriceList(),
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
                        "BrandId is blank",
                        null,
                        PRODUCT_ID,
                        DATE_TIME_UTC_PARAM,
                        EmptyInputException.class.toString(),
                        BRAND_ID_BLANK_EXCEPTION),
                Arguments.of(
                        "ProductId is blank",
                        BRAND_ID,
                        null,
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

    private static Stream<Arguments> provideTestCasesDateTimeParamNullOrBlank() {
        return Stream.of(Arguments.of(" "), Arguments.of((Object) null));
    }
}
