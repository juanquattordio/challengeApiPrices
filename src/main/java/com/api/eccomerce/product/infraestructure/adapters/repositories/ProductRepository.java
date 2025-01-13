package com.api.eccomerce.product.infraestructure.adapters.repositories;

import com.api.eccomerce.product.infraestructure.adapters.repositories.entities.PriceEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<PriceEntity, String> {}
