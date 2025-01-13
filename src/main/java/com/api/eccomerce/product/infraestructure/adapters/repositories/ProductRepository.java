package com.api.eccomerce.product.infraestructure.adapters.repositories;

import com.api.eccomerce.product.infraestructure.adapters.repositories.entities.PriceEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<PriceEntity, String> {
    @Query(
            "SELECT p FROM PriceEntity p WHERE :dateTime BETWEEN p.startDate AND p.endDate AND p.productId = :productId")
    List<PriceEntity> findPricesByDateTimeAndProduct(
            @Param("productId") String productId, @Param("dateTime") LocalDateTime dateTime);
}
