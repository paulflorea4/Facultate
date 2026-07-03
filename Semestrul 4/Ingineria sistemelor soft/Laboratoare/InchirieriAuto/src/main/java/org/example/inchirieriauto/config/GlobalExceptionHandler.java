package org.example.inchirieriauto.config;

import org.example.inchirieriauto.exception.BusinessRuleException;
import org.example.inchirieriauto.exception.CarNotFoundException;
import org.example.inchirieriauto.exception.UserNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public String handleDataAccess(DataAccessException ignored, Model model) {
        model.addAttribute("errorMessage", "Baza de date nu este disponibilă momentan. Încearcă din nou mai târziu.");
        return "error";
    }

    @ExceptionHandler({CarNotFoundException.class, UserNotFoundException.class})
    public String handleNotFound(RuntimeException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(BusinessRuleException.class)
    public String handleBusinessRule(BusinessRuleException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}

