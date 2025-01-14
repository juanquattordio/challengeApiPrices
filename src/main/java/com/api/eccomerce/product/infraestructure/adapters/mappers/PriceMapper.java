package com.api.eccomerce.product.infraestructure.adapters.mappers;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.repositories.entities.PriceEntity;
import com.api.eccomerce.product.infraestructure.controllers.responses.PriceResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    @Mapping(target = "startDateTime", source = "startDate")
    @Mapping(target = "endDateTime", source = "endDate")
    @Mapping(target = "value", source = "price")
    Price toDomain(PriceEntity priceEntity);

    PriceResponse toResponseDTO(Price priceDomain);
}
