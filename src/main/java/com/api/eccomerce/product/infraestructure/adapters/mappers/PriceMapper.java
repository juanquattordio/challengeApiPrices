package com.api.eccomerce.product.infraestructure.adapters.mappers;

import com.api.eccomerce.product.domain.models.Price;
import com.api.eccomerce.product.infraestructure.adapters.repositories.entities.PriceEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    //    List<Price> toDomain(List<PriceEntity> priceEntity);

    Price toDomain(PriceEntity priceEntity);
}
