package org.example.ducksocialnetworkui.validation;

import org.example.ducksocialnetworkui.domain.Persoana;
import org.example.ducksocialnetworkui.exception.ValidationException;

import java.time.LocalDate;

public class ValidatorPersoana implements Validator<Persoana>{
    @Override
    public void validate(Persoana p) throws ValidationException{
        StringBuilder errors = new StringBuilder();
        if (p.getUsername() == null || p.getUsername().isBlank())
            errors.append("Username invalid.\n");
        if (p.getEmail() == null || !p.getEmail().contains("@"))
            errors.append("Email invalid.\n");
        if (p.getNume() == null || p.getNume().isBlank())
            errors.append("Nume invalid.\n");
        if (p.getPrenume() == null || p.getPrenume().isBlank())
            errors.append("Prenume invalid.\n");
        if (p.getDataNasterii() == null || p.getDataNasterii().isAfter(LocalDate.now()))
            errors.append("Data nasterii invalida.\n");
        if(p.getOcupatie()==null || p.getOcupatie().isBlank())
            errors.append("Ocupatie invalida.\n");
        if (p.getNivelEmpatie() < 0)
            errors.append("Nivel empatie trebuie sa fie pozitiv.");

        if (!errors.isEmpty()) {
            throw new ValidationException(errors.toString());
        }
    }
}
