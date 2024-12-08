package com.sdk.kheeti.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sdk.kheeti.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndFarmerId(Long productId, Long farmerId);
    List<Product> findByFarmerId(Long farmerId);
}
