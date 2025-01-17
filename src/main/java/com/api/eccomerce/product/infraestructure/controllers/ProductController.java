package com.api.eccomerce.product.infraestructure.controllers;

import static com.api.eccomerce.product.infraestructure.exceptions.ExceptionMessages.*;

import com.api.eccomerce.product.application.usecases.ProductService;
import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;
import com.api.eccomerce.product.infraestructure.controllers.responses.BrandResponse;
import com.api.eccomerce.product.infraestructure.controllers.responses.ProductResponse;
import com.api.eccomerce.product.infraestructure.exceptions.EmptyInputException;
import com.api.eccomerce.product.infraestructure.exceptions.InvalidDateTimeFormatException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@Tag(
        name = "Product",
        description =
                "The Product API contains all the operations that can be performed on a product.")
public class ProductController {
    private final ProductService productService;
    private final PriceMapper priceMapper;

    @Autowired
    public ProductController(ProductService productService, PriceMapper priceMapper) {
        this.productService = productService;
        this.priceMapper = priceMapper;
    }

    @Operation(
            summary = "Get a price for a brand-product",
            description =
                    "If it exists, returns the price of a brand-product for a requested point in time")
    @ApiResponses(
            value = {
                @ApiResponse(
                                responseCode = "200",
                                description = "0 (none) or 1 (one) price founded",
                                content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema =
                                                    @Schema(implementation = ProductResponse.class))
                                }),
                        @ApiResponse(
                                responseCode = "400",
                                description = "brandId or productId blank",
                                content = @Content),
                @ApiResponse(
                                responseCode = "400",
                                description = "Invalid dateTimeUTC supplied",
                                content = @Content),
                        @ApiResponse(
                                responseCode = "500",
                                description = "Unexpected error. Used for the rest of the errors",
                                content = @Content)
            })
    @GetMapping("/{brandId}/{productId}/prices")
    public ProductResponse getPriceByBrandAndProductAndDateTime(
            @PathVariable String brandId,
            @PathVariable String productId,
            @RequestParam(value = "dateTimeUTC", required = false) String dateTimeUTC) {

        validateBrandIdAndProductId(brandId, productId);

        LocalDateTime handledDateTimeUTC = processDateTimeParam(dateTimeUTC);

        Optional<Price> price =
                productService.getPriceByBrandAndProductAndDateTime(
                        brandId, productId, handledDateTimeUTC);

        return ProductResponse.builder()
                .brand(new BrandResponse(brandId))
                .productCodeId(productId)
                .prices(price.stream().map(priceMapper::toResponseDTO).toList())
                .build();
    }

    private LocalDateTime processDateTimeParam(String dateTimeUTC) {
        if (dateTimeUTC == null || dateTimeUTC.isBlank()) {
            return LocalDateTime.now(ZoneId.of("UTC"));
        }
        try {
            OffsetDateTime offsetDateTime =
                    OffsetDateTime.parse(dateTimeUTC, DateTimeFormatter.ISO_DATE_TIME);

            if (!offsetDateTime.getOffset().equals(ZoneOffset.UTC)) {
                throw new InvalidDateTimeFormatException(INVALID_DATE_TIME_EXCEPTION);
            }

            return offsetDateTime.toLocalDateTime();
        } catch (DateTimeParseException e) {
            throw new InvalidDateTimeFormatException(INVALID_DATE_TIME_EXCEPTION);
        }
    }

    private void validateBrandIdAndProductId(String brandId, String productId) {
        if (brandId == null || brandId.trim().isEmpty()) {
            throw new EmptyInputException(BRAND_ID_BLANK_EXCEPTION);
        }
        if (productId == null || productId.trim().isEmpty()) {
            throw new EmptyInputException(PRODUCT_ID_BLANK_EXCEPTION);
        }
    }
}
