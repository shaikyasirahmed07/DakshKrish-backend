package com.sdk.kheeti.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sdk.kheeti.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email); // Optional: For login validation
}
