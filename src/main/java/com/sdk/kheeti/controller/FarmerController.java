package com.sdk.kheeti.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sdk.kheeti.model.Farmer;
import com.sdk.kheeti.model.Product;
import com.sdk.kheeti.service.FarmerService;
import com.sdk.kheeti.service.ProductService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/farmers")
public class FarmerController {

    @Autowired
    private FarmerService farmerService;

    @Autowired
    private ProductService productService;

    // Farmer registration
    @PostMapping("/farmer-register")
    public ResponseEntity<?> registerFarmer(@RequestBody Farmer farmer) {
        try {
            farmer.setApproved(false); // Default approval to false
            Farmer createdFarmer = farmerService.createFarmer(farmer);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Farmer registered successfully. Pending admin approval.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error registering farmer: " + e.getMessage());
        }
    }

    // Farmer login
    @PostMapping("/farmer-login")
    public ResponseEntity<?> loginFarmer(@RequestBody Map<String, String> loginDetails) {
        try {
            String email = loginDetails.get("email");
            String password = loginDetails.get("password");

            Farmer farmer = farmerService.authenticateFarmer(email, password);

            if (farmer == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
            }

            if (!farmer.isApproved()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account pending admin approval.");
            }

            return ResponseEntity.ok(Map.of(
                    "farmerId", farmer.getId(),
                    "message", "Login successful"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during login: " + e.getMessage());
        }
    }

    // Get all products for a specific farmer
    @GetMapping("/{farmerId}/products")
    public ResponseEntity<?> getFarmerProducts(@PathVariable Long farmerId) {
        try {
            List<Product> products = productService.getProductsByFarmerId(farmerId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching products: " + e.getMessage());
        }
    }



    @PutMapping("/{farmerId}/edit-product/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long farmerId,
                                           @PathVariable Long productId,
                                           @RequestBody Product updatedProduct) {
        try {
            Product updated = productService.updateProduct(farmerId, productId, updatedProduct);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating product: " + e.getMessage());
        }
    }
    @GetMapping("/{farmerId}/products/{productId}")
public ResponseEntity<?> getProductDetails(@PathVariable Long farmerId, @PathVariable Long productId) {
    try {
        Product product = productService.getProductByIdAndFarmerId(productId, farmerId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found for the given farmer.");
        }
        return ResponseEntity.ok(product);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching product details: " + e.getMessage());
    }
}

    @PostMapping("/{farmerId}/add-product")
    public ResponseEntity<?> addProduct(
            @PathVariable Long farmerId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("category") String category,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Product newProduct = productService.addProduct(farmerId, name, description, price, category, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding product: " + e.getMessage());
        }
    }


}
