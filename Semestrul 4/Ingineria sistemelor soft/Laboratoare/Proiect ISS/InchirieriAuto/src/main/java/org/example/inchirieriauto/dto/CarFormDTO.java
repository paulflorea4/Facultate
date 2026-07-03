package org.example.inchirieriauto.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.inchirieriauto.model.CarStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuppressWarnings("unused")
public class CarFormDTO {
    private Integer id;

    @NotBlank(message = "Marca este obligatorie.")
    @Size(max = 20, message = "Marca poate avea cel mult 20 de caractere.")
    private String brand;

    @NotBlank(message = "Modelul este obligatoriu.")
    @Size(max = 20, message = "Modelul poate avea cel mult 20 de caractere.")
    private String model;

    @Min(value = 1900, message = "Anul nu este valid.")
    @Max(value = 2100, message = "Anul nu este valid.")
    private int year;

    @Positive(message = "Prețul pe zi trebuie să fie pozitiv.")
    private double pricePerDay;

    @NotNull(message = "Statusul mașinii este obligatoriu.")
    private CarStatus status;

    private List<Integer> featureIds = new ArrayList<>();
}

