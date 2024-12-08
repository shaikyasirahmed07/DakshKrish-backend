package com.sdk.kheeti.repositories.RepoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.sdk.kheeti.model.Farmer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository
public class FarRepoImpl {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Farmer authenticateFarmer(String email, String password) {
        // Create a query to find the farmer by email
        TypedQuery<Farmer> query = entityManager.createQuery(
            "SELECT f FROM Farmer f WHERE f.email = :email", Farmer.class);
        query.setParameter("email", email);
        
        // Get the farmer
        Farmer farmer = query.getResultList().stream().findFirst().orElse(null);

        // Check if the farmer exists and the password matches
        if (farmer != null) {
            // Use BCrypt to match the password
            if (passwordEncoder.matches(password, farmer.getPassword())) {
                return farmer; // Successful authentication
            }
        }

        return null; // Authentication failed
    }
}
