package com.sdk.kheeti.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdk.kheeti.model.SoldProduct;

public interface SoldProductRepository extends JpaRepository<SoldProduct, Long> {
    List<SoldProduct> findByFarmerId(Long farmerId);  // Use 'farmerId' if that's the field name
}
