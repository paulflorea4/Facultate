package org.example.inchirieriauto.service;

import org.example.inchirieriauto.exception.CarNotFoundException;
import org.example.inchirieriauto.model.Car;
import org.example.inchirieriauto.model.CarStatus;
import org.example.inchirieriauto.dto.CarFilterDTO;
import org.example.inchirieriauto.repository.CarRepository;
import org.example.inchirieriauto.repository.specification.CarSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getVisibleCars(CarFilterDTO filter, boolean availableOnly) {
        Specification<Car> spec = CarSpecification.getCarsByFilters(filter, availableOnly);
        return carRepository.findAll(spec);
    }

    public Car getCarById(Integer id, boolean admin) {
        Car car = carRepository.findByIdWithFeatures(id)
                .orElseGet(() -> carRepository.findById(id)
                        .orElseThrow(() -> new CarNotFoundException(id)));


        if (!admin && car.getStatus() != CarStatus.AVAILABLE) {
            throw new CarNotFoundException(id);
        }

        return car;
    }
}