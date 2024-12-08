package com.sdk.kheeti.service;

import com.sdk.kheeti.model.Farmer;
import com.sdk.kheeti.model.FarmerProfile;
import com.sdk.kheeti.repositories.FarmerProfileRepo;
import com.sdk.kheeti.repositories.FarmerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class FarmerProfileService {

    @Autowired
    private FarmerProfileRepo farmerProfileRepository;

    @Autowired
    private FarmerRepository farmerRepository;

    /**
     * Get Farmer Profile by Farmer ID
     */
    public Optional<FarmerProfile> getFarmerProfileByFarmerId(Long farmerId) {
        return farmerProfileRepository.findByFarmerId(farmerId);
    }

    /**
     * Create or Update Farmer Profile
     */
    public FarmerProfile createOrUpdateProfile(Long farmerId, String firstName, String fullName, String address,
                                               Double farmSize, MultipartFile profileImage, MultipartFile farmImage) throws IOException {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new RuntimeException("Farmer not found with ID: " + farmerId));

        FarmerProfile profile = farmerProfileRepository.findByFarmerId(farmerId).orElse(new FarmerProfile());
        profile.setFarmer(farmer);
        profile.setFirstName(firstName);
        profile.setFullName(fullName);
        profile.setAddress(address);
        profile.setFarmSize(farmSize);

        if (profileImage != null && !profileImage.isEmpty()) {
            profile.setProfileImage(profileImage.getBytes());
        }

        if (farmImage != null && !farmImage.isEmpty()) {
            profile.setFarmImage(farmImage.getBytes());
        }

        return farmerProfileRepository.save(profile);
    }

    /**
     * Delete Farmer Profile by Farmer ID
     */
    public void deleteFarmerProfileByFarmerId(Long farmerId) {
        FarmerProfile profile = farmerProfileRepository.findByFarmerId(farmerId)
                .orElseThrow(() -> new RuntimeException("Profile not found for Farmer ID: " + farmerId));
        farmerProfileRepository.delete(profile);
    }
}
