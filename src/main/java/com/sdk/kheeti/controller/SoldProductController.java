package com.sdk.kheeti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdk.kheeti.model.SoldProduct;
import com.sdk.kheeti.service.SoldProductService;

@RestController
@RequestMapping("/api/farmers/sold-products")
@CrossOrigin(origins = "*")
public class SoldProductController {

    @Autowired
    private SoldProductService soldProductService;

    @GetMapping("/{farmerId}")
    public ResponseEntity<?> getSoldProducts(@PathVariable Long farmerId) {
        try {
            List<SoldProduct> soldProducts = soldProductService.getSoldProductsByFarmer(farmerId);
            return ResponseEntity.ok(soldProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching sold products: " + e.getMessage());
        }
    }

    @PostMapping("/{farmerId}")
    public ResponseEntity<?> addSoldProduct(@PathVariable Long farmerId, @RequestBody SoldProduct soldProduct) {
        try {
            SoldProduct savedProduct = soldProductService.addSoldProduct(farmerId, soldProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error adding sold product: " + e.getMessage());
        }
    }
}
