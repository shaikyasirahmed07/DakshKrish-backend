package com.sdk.kheeti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdk.kheeti.model.Farmer;
import com.sdk.kheeti.model.SoldProduct;
import com.sdk.kheeti.repositories.FarmerRepository;
import com.sdk.kheeti.repositories.SoldProductRepository;

@Service
public class SoldProductService {

    @Autowired
    private SoldProductRepository soldProductRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    public List<SoldProduct> getSoldProductsByFarmer(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));
        return soldProductRepository.findByFarmer(farmer);
    }

    public SoldProduct addSoldProduct(Long farmerId, SoldProduct soldProduct) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        soldProduct.setFarmer(farmer);
        return soldProductRepository.save(soldProduct);
    }
}
