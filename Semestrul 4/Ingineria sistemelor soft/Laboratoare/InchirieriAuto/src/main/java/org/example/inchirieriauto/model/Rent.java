package org.example.inchirieriauto.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "rents")
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Timestamp startDate;
    private Timestamp endDate;

    @Enumerated(EnumType.STRING)
    private RentStatus status;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}