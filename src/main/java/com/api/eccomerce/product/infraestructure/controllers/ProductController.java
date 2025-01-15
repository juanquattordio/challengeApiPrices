package com.api.eccomerce.product.infraestructure.controllers;

import static com.api.eccomerce.product.domain.valueObjetcs.Brand.getBrandByBrandId;

import com.api.eccomerce.product.application.usecases.ProductService;
import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.domain.valueObjetcs.Brand;
import com.api.eccomerce.product.infraestructure.adapters.mappers.PriceMapper;
import com.api.eccomerce.product.infraestructure.controllers.responses.BrandResponse;
import com.api.eccomerce.product.infraestructure.controllers.responses.ProductResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

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
                .brand(createResponse(brandId))
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

    private BrandResponse createResponse(String brandId) {
        try {
            Brand brand = getBrandByBrandId(brandId);
            return BrandResponse.builder()
                    .brandId(brand.getId())
                    .description(brand.getDescription())
                    .build();
        } catch (IllegalArgumentException e) {
            logger.info("Error creating brand response [{}]", e.getMessage());
        }
        return BrandResponse.builder().brandId(brandId).build();
    }
}
