package com.sdk.kheeti.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdk.kheeti.model.Admin;
import com.sdk.kheeti.model.Farmer;
import com.sdk.kheeti.model.Product;
import com.sdk.kheeti.service.AdminService;
import com.sdk.kheeti.service.FarmerService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private FarmerService farmerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Helper method for token validation (example)
    private boolean isValidToken(String token) {
        return token != null && token.startsWith("session_"); // Simple validation for session tokens
    }

    /**
     * Admin Signup Endpoint
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signupAdmin(@RequestBody Admin admin) {
        try {
            // Validate password
            if (admin.getPassword() == null || admin.getPassword().length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Password must be at least 6 characters long."));
            }
            // Validate email format
            if (admin.getEmail() == null
                    || !admin.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid email format."));
            }

            // Encrypt the password before saving
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            Admin createdAdmin = adminService.createAdmin(admin);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Admin created successfully.", "admin", createdAdmin));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error creating admin: " + e.getMessage()));
        }
    }

    /**
     * Admin Login Endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {
        try {
            Admin existingAdmin = adminService.findByEmail(admin.getEmail());

            if (existingAdmin == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Admin does not exist."));
            }

            if (!passwordEncoder.matches(admin.getPassword(), existingAdmin.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid password."));
            }

            String token = "session_" + existingAdmin.getId(); // Example token generation (simplified)
            return ResponseEntity
                    .ok(Map.of("token", token, "adminId", existingAdmin.getId(), "message", "Login successful."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error during login: " + e.getMessage()));
        }
    }

    /**
     * Get All Farmers
     */
    @GetMapping("/farmers")
    public ResponseEntity<?> getAllFarmers(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token."));
        }
        String authToken = token.substring(7); // Extract token without 'Bearer ' prefix
        if (!isValidToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token."));
        }

        try {
            List<Farmer> farmers = farmerService.getAllFarmers();
            return ResponseEntity.ok(farmers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching farmers: " + e.getMessage()));
        }
    }

    @GetMapping("/{farmerId}/products")
    public ResponseEntity<?> getFarmerProducts(@PathVariable Long farmerId) {
        try {
            List<Product> products = farmerService.getProductsByFarmerId(farmerId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error fetching products: " + e.getMessage()));
        }
    }

    /**
     * Approve a Farmer
     */
    @PostMapping("/farmers/approve/{farmerId}")
    public ResponseEntity<?> approveFarmer(@RequestHeader("Authorization") String token, @PathVariable Long farmerId) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token."));
        }
        String authToken = token.substring(7); // Extract token without 'Bearer ' prefix
        if (!isValidToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token."));
        }

        try {
            Farmer approvedFarmer = farmerService.approveFarmer(farmerId);
            return ResponseEntity.ok(Map.of("message", "Farmer approved successfully.", "farmer", approvedFarmer));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Error approving farmer: " + e.getMessage()));
        }
    }

    /**
     * Delete a Farmer
     */
    @DeleteMapping("/farmers/{farmerId}")
    public ResponseEntity<?> deleteFarmer(@RequestHeader("Authorization") String token, @PathVariable Long farmerId) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token."));
        }
        String authToken = token.substring(7); // Extract token without 'Bearer ' prefix
        if (!isValidToken(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token."));
        }

        try {
            farmerService.deleteFarmer(farmerId);
            return ResponseEntity.ok(Map.of("message", "Farmer deleted successfully."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Error deleting farmer: " + e.getMessage()));
        }
    }


    @GetMapping("/farmers/{farmerId}")
    public ResponseEntity<?> getFarmerDetails(@PathVariable Long farmerId) {
        try {
            Farmer farmer = farmerService.getFarmerById(farmerId);
            return ResponseEntity.ok(farmer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Farmer not found: " + e.getMessage()));
        }
    }

    // Get products by farmer
    @GetMapping("/farmers/{farmerId}/products")
    public ResponseEntity<?> getFarmerProducts(@PathVariable Long farmerId) {
        try {
            List<Product> products = farmerService.getProductsByFarmerId(farmerId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Error fetching products: " + e.getMessage()));
        }
    }
}
