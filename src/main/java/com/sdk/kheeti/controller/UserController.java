package com.sdk.kheeti.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sdk.kheeti.model.Product;
import com.sdk.kheeti.model.User;
import com.sdk.kheeti.service.ProductService;
import com.sdk.kheeti.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * Register a new user.
     *
     * @param user The User object with registration details.
     * @return ResponseEntity with status and message.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok().body("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Authenticate and log in the user.
     *
     * @param user The User object containing login details.
     * @return ResponseEntity with userId and role if successful.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            User authenticatedUser = userService.loginUser(user.getEmail(), user.getPassword());
            return ResponseEntity.ok().body(Map.of(
                "userId", authenticatedUser.getId(),
                "role", authenticatedUser.getRole(),
                "message", "Login successful!"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Fetch all products from all farmers.
     *
     * @return List of products.
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(null);
        }
    }
}
