package org.example.inchirieriauto.repository;

import org.example.inchirieriauto.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    Optional<Feature> findByNameIgnoreCase(String name);
}
