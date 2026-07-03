package org.example.inchirieriauto.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class CarFilterDTO {
    @Size(max = 50, message = "Marca poate avea cel mult 50 de caractere.")
    private String brand;

    @Size(max = 50, message = "Modelul poate avea cel mult 50 de caractere.")
    private String model;

    @PositiveOrZero(message = "Prețul minim nu poate fi negativ.")
    private Double minPrice;

    @PositiveOrZero(message = "Prețul maxim nu poate fi negativ.")
    private Double maxPrice;

    @Min(value = 1886, message = "Anul nu este valid.")
    @Max(value = 2100, message = "Anul nu este valid.")
    private Integer year;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    @SuppressWarnings("unused")
    @AssertTrue(message = "Perioada de căutare este invalidă. Data de ridicare trebuie să fie înaintea datei de returnare.")
    public boolean isDateRangeValid() {
        if (startDate == null && endDate == null) {
            return true;
        }

        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }

    @SuppressWarnings("unused")
    @AssertTrue(message = "Prețul minim nu poate fi mai mare decât prețul maxim.")
    public boolean isPriceRangeValid() {
        if (minPrice == null || maxPrice == null) {
            return true;
        }

        return minPrice <= maxPrice;
    }
}
