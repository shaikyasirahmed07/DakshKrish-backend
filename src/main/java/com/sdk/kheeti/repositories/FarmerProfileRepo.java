package com.sdk.kheeti.repositories;

import com.sdk.kheeti.model.FarmerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FarmerProfileRepo extends JpaRepository<FarmerProfile, Long> {
    Optional<FarmerProfile> findByFarmerId(Long farmerId);
}
