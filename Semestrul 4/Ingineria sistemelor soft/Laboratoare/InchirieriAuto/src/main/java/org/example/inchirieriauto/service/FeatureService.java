package org.example.inchirieriauto.service;

import jakarta.transaction.Transactional;
import org.example.inchirieriauto.dto.FeatureFormDTO;
import org.example.inchirieriauto.exception.BusinessRuleException;
import org.example.inchirieriauto.model.Car;
import org.example.inchirieriauto.model.Feature;
import org.example.inchirieriauto.repository.CarRepository;
import org.example.inchirieriauto.repository.FeatureRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class FeatureService {
    private final FeatureRepository featureRepository;
    private final CarRepository carRepository;

    public FeatureService(FeatureRepository featureRepository, CarRepository carRepository) {
        this.featureRepository = featureRepository;
        this.carRepository = carRepository;
    }

    public List<Feature> getAllFeatures() {
        return featureRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public Set<Feature> getFeaturesByIds(List<Integer> featureIds) {
        if (featureIds == null || featureIds.isEmpty()) {
            return new LinkedHashSet<>();
        }

        Set<Feature> features = new LinkedHashSet<>();
        featureRepository.findAllById(featureIds).forEach(features::add);
        if (features.size() != featureIds.stream().distinct().count()) {
            throw new BusinessRuleException("Una sau mai multe dotări selectate nu au fost găsite.");
        }
        return features;
    }

    public Feature getFeature(Integer id) {
        return featureRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Dotarea nu a fost găsită."));
    }

    @Transactional
    public Feature saveFeature(FeatureFormDTO dto) {
        validateUniqueName(dto.getId(), dto.getName());

        Feature feature = dto.getId() == null ? new Feature() : getFeature(dto.getId());
        feature.setName(dto.getName().trim());
        return featureRepository.save(feature);
    }

    @Transactional
    public void deleteFeature(Integer id) {
        Feature feature = getFeature(id);
        List<Car> cars = carRepository.findByFeaturesId(id);
        for (Car car : cars) {
            if (car.getFeatures() != null) {
                car.getFeatures().removeIf(existing -> Objects.equals(existing.getId(), id));
                carRepository.save(car);
            }
        }
        featureRepository.delete(feature);
    }

    private void validateUniqueName(Integer featureId, String name) {
        featureRepository.findByNameIgnoreCase(name == null ? null : name.trim()).ifPresent(existing -> {
            if (featureId == null || !existing.getId().equals(featureId)) {
                throw new BusinessRuleException("Există deja o dotare cu acest nume.");
            }
        });
    }
}



