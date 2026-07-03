package org.example.inchirieriauto.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "clients")
@Getter @Setter @NoArgsConstructor
public class Client extends User {

    @NotBlank(message = "Numele este obligatoriu")
    @Size(min = 3, max = 50, message = "Numele trebuie să aibă între 3 și 50 caractere")
    @Column(nullable = false,  length = 50)
    private String name;

    @OneToMany(mappedBy = "client")
    private List<Rent> rents;
}
