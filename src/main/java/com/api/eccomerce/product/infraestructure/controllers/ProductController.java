package com.api.eccomerce.product.infraestructure.controllers;

import com.api.eccomerce.product.application.usecases.ProductService;
import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;
import com.api.eccomerce.product.infraestructure.controllers.responses.ProductResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final PriceMapper priceMapper;

    @Autowired
    public ProductController(ProductService productService, PriceMapper priceMapper) {
        this.productService = productService;
        this.priceMapper = priceMapper;
    }

    @GetMapping("/{brandId}/{productId}/prices")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getPriceByProductAndDateTime(
            @PathVariable String brandId,
            @PathVariable String productId,
            @RequestParam(value = "dateTimeUTC", required = false)
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime dateTimeUTC) {
        LocalDateTime handledDateTimeUTC = handleDateTimeParam(dateTimeUTC);
        Optional<Price> price =
                productService.getPriceByBrandAndProductAndDateTime(
                        brandId, productId, handledDateTimeUTC);

        return ProductResponse.builder()
                .brandId(brandId)
                .productCodeId(productId)
                .prices(price.stream().map(priceMapper::toResponseDTO).toList())
                .build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        "Invalid date and time format. Please provide the date and time in UTC using the valid format: yyyy-MM-ddThh:mm:ss. "
                                + ex.getMessage());
    }

    private LocalDateTime handleDateTimeParam(LocalDateTime dateTimeUTC) {
        return dateTimeUTC != null ? dateTimeUTC : LocalDateTime.now(ZoneId.of("UTC"));
    }
}
