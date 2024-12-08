package com.sdk.kheeti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sdk.kheeti.model.Farmer;
import com.sdk.kheeti.repositories.FarmerRepository;

@Service
public class FarmerRegistrationService {

    @Autowired
    private FarmerRepository farmerRepo; // Your Farmer repository

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // BCrypt password encoder

    public void registerFarmer(Farmer farmer) {
        // Hash the plain-text password before saving it to the database
        String hashedPassword = passwordEncoder.encode(farmer.getPassword());
        farmer.setPassword(hashedPassword);
        
        // Save the farmer entity in the database
        farmerRepo.save(farmer);
    }
}
