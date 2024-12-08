package com.sdk.kheeti.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sdk.kheeti.model.Product;
import com.sdk.kheeti.repositories.ProductRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    // Define UPLOAD_DIR using application properties
    @Value("${file.upload-dir:/Users/yasirahmedshaik/images}")
    private String uploadDir;

    /**
     * Get all products for a specific farmer.
     */
    public List<Product> getProductsByFarmerId(Long farmerId) {
        return productRepo.findByFarmerId(farmerId);
    }

    /**
     * Get a product by its ID and farmer ID.
     */
    public Product getProductByIdAndFarmerId(Long productId, Long farmerId) throws Exception {
        return productRepo.findByIdAndFarmerId(productId, farmerId)
                .orElseThrow(() -> new Exception("Product not found or unauthorized access"));
    }

    /**
     * Update a product.
     */
    public Product updateProduct(Long farmerId, Long productId, Product updatedProduct) throws Exception {
        Product existingProduct = productRepo.findByIdAndFarmerId(productId, farmerId)
                .orElseThrow(() -> new Exception("Product not found or unauthorized access"));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());

        return productRepo.save(existingProduct);
    }

    /**
     * Get all products.
     */
    public List<Product> getAllProducts() {
        try {
            return productRepo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching products: " + e.getMessage());
        }
    }

    /**
     * Add a new product for a farmer.
     */
    public Product addProduct(Long farmerId, String name, String description, Double price, String category, MultipartFile image) {
        Product product = new Product();
        product.setFarmerId(farmerId);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);

        if (image != null && !image.isEmpty()) {
            try {
                File uploadDirectory = new File(uploadDir);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.write(filePath, image.getBytes());
                product.setImageUrl(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error saving image file: " + e.getMessage());
            }
        }

        return productRepo.save(product);
    }
}
