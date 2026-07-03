package org.example.inchirieriauto.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.inchirieriauto.validation.UniqueEmail;

@Setter
@Getter
public class RegistrationDTO {

    @NotBlank(message = "Numele este obligatoriu.")
    @Size(min = 3, max = 50, message = "Numele trebuie să aibă între 3 și 50 caractere.")
    private String name;

    @NotBlank(message = "Email-ul este obligatoriu.")
    @Email(message = "Email-ul trebuie să fie valid.")
    @UniqueEmail(message = "Email-ul este deja înregistrat în sistem.")
    private String email;

    @NotBlank(message = "Parola este obligatorie.")
    @Size(min = 6, message = "Parola trebuie să aibă cel puțin 6 caractere.")
    private String password;

    public RegistrationDTO() {}

    public RegistrationDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}

