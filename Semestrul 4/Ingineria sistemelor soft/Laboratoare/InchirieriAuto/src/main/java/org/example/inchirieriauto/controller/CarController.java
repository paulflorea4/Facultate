package org.example.inchirieriauto.controller;

import org.example.inchirieriauto.exception.CarNotFoundException;
import org.example.inchirieriauto.model.Car;
import org.example.inchirieriauto.dto.CarFilterDTO;
import org.example.inchirieriauto.service.CarService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

@Controller
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String listCars(@Valid @ModelAttribute("filter") CarFilterDTO filter,
                           BindingResult bindingResult,
                           Authentication authentication,
                           Model model) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (bindingResult.hasErrors()) {
            model.addAttribute("cars", Collections.emptyList());
            model.addAttribute("errorMessage", "Există filtre invalide. Verifică valorile introduse și încearcă din nou.");
            return "car-list";
        }

        try {
            model.addAttribute("cars", carService.getVisibleCars(filter, !isAdmin));
        } catch (DataAccessException ex) {
            model.addAttribute("cars", Collections.emptyList());
            model.addAttribute("errorMessage", "Baza de date nu este disponibilă momentan. Încearcă din nou mai târziu.");
        }
        return "car-list";
    }

    @GetMapping("/{id}")
    public String viewCar(@PathVariable Integer id,
                          Authentication authentication,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        try {
            Car car = carService.getCarById(id, isAdmin);
            model.addAttribute("car", car);
            return "car-details";
        } catch (CarNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/cars";
        } catch (DataAccessException ex) {
            model.addAttribute("errorMessage", "Nu putem încărca detaliile mașinii momentan. Baza de date nu răspunde.");
            return "error";
        }
    }
}
