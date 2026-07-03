package org.example.inchirieriauto.service;

import jakarta.transaction.Transactional;
import org.example.inchirieriauto.dto.CarFormDTO;
import org.example.inchirieriauto.exception.BusinessRuleException;
import org.example.inchirieriauto.exception.CarNotFoundException;
import org.example.inchirieriauto.model.Car;
import org.example.inchirieriauto.model.CarStatus;
import org.example.inchirieriauto.repository.CarRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCarService {
    private final CarRepository carRepository;
    private final FeatureService featureService;
    private final RentService rentService;

    public AdminCarService(CarRepository carRepository, FeatureService featureService, RentService rentService) {
        this.carRepository = carRepository;
        this.featureService = featureService;
        this.rentService = rentService;
    }

    public List<Car> getDashboardCars() {
        return carRepository.findAll(Sort.by(Sort.Direction.ASC, "brand", "model"));
    }

    public CarFormDTO getCarForm(Integer carId) {
        if (carId == null) {
            return new CarFormDTO();
        }

        Car car = carRepository.findByIdWithFeatures(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
        return toForm(car);
    }

    @Transactional
    public Car saveCar(CarFormDTO dto) {
        validateStatus(dto.getStatus());
        Car car = dto.getId() == null ? new Car() : carRepository.findById(dto.getId())
                .orElseThrow(() -> new CarNotFoundException(dto.getId()));

        CarStatus oldStatus = car.getStatus();
        applyForm(car, dto);
        Car saved = carRepository.save(car);

        if (shouldCancelRents(oldStatus, saved.getStatus())) {
            rentService.cancelPendingRentsForCar(saved.getId());
        }

        return saved;
    }

    @Transactional
    public void deleteCar(Integer carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));
        
        if (rentService.hasActiveRentals(carId)) {
            throw new BusinessRuleException("Nu puteți șterge o mașină care este rezervată. Anulați mai întâi toate rezervările active.");
        }
        
        rentService.cancelPendingRentsForCar(carId);
        
        if (car.getFeatures() != null) {
            car.getFeatures().clear();
        }
        carRepository.save(car);
        
        carRepository.delete(car);
    }

    private void applyForm(Car car, CarFormDTO dto) {
        car.setBrand(dto.getBrand().trim());
        car.setModel(dto.getModel().trim());
        car.setYear(dto.getYear());
        car.setPricePerDay(dto.getPricePerDay());
        car.setStatus(dto.getStatus());
        car.setFeatures(featureService.getFeaturesByIds(dto.getFeatureIds()));
    }

    private CarFormDTO toForm(Car car) {
        CarFormDTO dto = new CarFormDTO();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setYear(car.getYear());
        dto.setPricePerDay(car.getPricePerDay());
        dto.setStatus(car.getStatus());
        if (car.getFeatures() != null) {
            dto.setFeatureIds(car.getFeatures().stream().map(feature -> feature.getId()).toList());
        }
        return dto;
    }

    private boolean shouldCancelRents(CarStatus oldStatus, CarStatus newStatus) {
        if (newStatus == CarStatus.SERVICE) {
            return oldStatus != newStatus;
        }
        return false;
    }

    private void validateStatus(CarStatus status) {
        if (status == null) {
            throw new BusinessRuleException("Statusul mașinii este obligatoriu.");
        }
    }
}

