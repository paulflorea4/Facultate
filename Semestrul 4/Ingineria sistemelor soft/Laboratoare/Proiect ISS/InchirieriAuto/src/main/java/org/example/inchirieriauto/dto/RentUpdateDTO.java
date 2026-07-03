package org.example.inchirieriauto.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@SuppressWarnings("unused")
public class RentUpdateDTO {
    private Integer rentId;

    @NotNull(message = "Data de ridicare este obligatorie.")
    @FutureOrPresent(message = "Data de ridicare nu poate fi în trecut.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    @NotNull(message = "Data de returnare este obligatorie.")
    @FutureOrPresent(message = "Data de returnare nu poate fi în trecut.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    @AssertTrue(message = "Data de ridicare trebuie să fie înaintea datei de returnare.")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return startDate.isBefore(endDate);
    }
}

