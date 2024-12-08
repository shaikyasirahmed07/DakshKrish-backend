package com.sdk.kheeti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sdk.kheeti.model.Farmer;
import com.sdk.kheeti.model.Product;
import com.sdk.kheeti.repositories.FarmerRepository;
import com.sdk.kheeti.repositories.ProductRepo;


@Service
public class FarmerService {

    @Autowired
    private FarmerRepository farmerRepository;

    @Autowired
    private ProductRepo productRepo; // Repository for fetching products

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new farmer.
     */
    public Farmer createFarmer(Farmer farmer) {
        farmer.setPassword(passwordEncoder.encode(farmer.getPassword())); // Encrypt password
        farmer.setApproved(false); // Default to not approved
        return farmerRepository.save(farmer);
    }

    /**
     * Get all farmers.
     */
    public List<Farmer> getAllFarmers() {
        return farmerRepository.findAll();
    }

    /**
     * Approve a farmer by their ID.
     */
    public Farmer approveFarmer(Long farmerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
            .orElseThrow(() -> new RuntimeException("Farmer not found"));
        farmer.setApproved(true);
        return farmerRepository.save(farmer);
    }

    /**
     * Delete a farmer by their ID.
     */
    public void deleteFarmer(Long farmerId) {
        if (!farmerRepository.existsById(farmerId)) {
            throw new RuntimeException("Farmer not found");
        }
        farmerRepository.deleteById(farmerId);
    }

    /**
     * Authenticate a farmer using their email and password.
     */
    public Farmer authenticateFarmer(String email, String password) {
        Farmer farmer = farmerRepository.findByEmail(email);
        if (farmer != null && passwordEncoder.matches(password, farmer.getPassword())) {
            return farmer;
        }
        return null;
    }

    /**
     * Get all products associated with a specific farmer.
     */
    public List<Product> getProductsByFarmerId(Long farmerId) {
        // Fetch farmer from the repository
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found"));

        // Fetch products using the repository instance
        return productRepo.findByFarmer(farmer); // Use correct method
    }




    public Farmer getFarmerById(Long farmerId) throws Exception {
        return farmerRepository.findById(farmerId)
                .orElseThrow(() -> new Exception("Farmer not found with ID: " + farmerId));
    }

    // public List<Product> getProductsByFarmerId(Long farmerId) {
    //     return productRepository.findByFarmerId(farmerId);
    // }
}
