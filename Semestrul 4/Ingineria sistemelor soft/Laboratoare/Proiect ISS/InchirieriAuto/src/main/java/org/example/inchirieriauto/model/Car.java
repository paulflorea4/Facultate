package org.example.inchirieriauto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,  length = 20)
    private String brand;
    @Column(nullable = false,  length = 20)
    private String model;
    private int year;

    @Column(name = "price_per_day")
    private double pricePerDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,  length = 10)
    private CarStatus status;

    @ManyToMany
    @JoinTable(
            name = "car_features",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Feature> features;
}
