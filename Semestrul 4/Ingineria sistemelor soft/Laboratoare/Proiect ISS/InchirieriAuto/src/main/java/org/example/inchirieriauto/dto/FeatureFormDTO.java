package org.example.inchirieriauto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class FeatureFormDTO {
    private Integer id;

    @NotBlank(message = "Numele dotării este obligatoriu.")
    @Size(max = 50, message = "Numele dotării poate avea cel mult 50 de caractere.")
    private String name;
}

