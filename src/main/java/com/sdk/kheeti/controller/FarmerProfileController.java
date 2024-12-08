package com.sdk.kheeti.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sdk.kheeti.model.FarmerProfile;
import com.sdk.kheeti.service.FarmerProfileService;
import com.sdk.kheeti.service.FarmerService;

@RestController
@RequestMapping("/api/farmers")
@CrossOrigin(origins = "*")
public class FarmerProfileController {

    @Autowired
    private FarmerProfileService farmerProfileService;

    @Autowired
    private FarmerService farmerService;

    /**
     * Get Farmer Profile by Farmer ID
     */
    @GetMapping("/{farmerId}/profile")
    public ResponseEntity<?> getFarmerProfile(@PathVariable Long farmerId) {
        try {
            Optional<FarmerProfile> profile = farmerProfileService.getFarmerProfileByFarmerId(farmerId);
            if (profile.isPresent()) {
                return ResponseEntity.ok(profile.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found for Farmer ID: " + farmerId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching profile: " + e.getMessage());
        }
    }

    /**
     * Create or Update Farmer Profile
     */
    @PostMapping("/{farmerId}/profile")
    public ResponseEntity<?> createOrUpdateFarmerProfile(
            @PathVariable Long farmerId,
            @RequestParam("firstName") String firstName,
            @RequestParam("fullName") String fullName,
            @RequestParam("address") String address,
            @RequestParam("farmSize") Double farmSize,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "farmImage", required = false) MultipartFile farmImage) {
        try {
            FarmerProfile profile = farmerProfileService.createOrUpdateProfile(farmerId, firstName, fullName, address, farmSize, profileImage, farmImage);
            return ResponseEntity.ok(profile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error saving profile: " + e.getMessage());
        }
    }
    

    /**
     * Delete Farmer Profile by Farmer ID
     */
    @DeleteMapping("/{farmerId}/profile")
    public ResponseEntity<?> deleteFarmerProfile(@PathVariable Long farmerId) {
        try {
            farmerProfileService.deleteFarmerProfileByFarmerId(farmerId);
            return ResponseEntity.ok("Profile deleted successfully for Farmer ID: " + farmerId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting profile: " + e.getMessage());
        }
    }
}
