package org.example.inchirieriauto.controller;

import jakarta.validation.Valid;
import org.example.inchirieriauto.dto.RentRequestDTO;
import org.example.inchirieriauto.dto.RentUpdateDTO;
import org.example.inchirieriauto.exception.BusinessRuleException;
import org.example.inchirieriauto.exception.CarNotFoundException;
import org.example.inchirieriauto.model.Car;
import org.example.inchirieriauto.service.CarService;
import org.example.inchirieriauto.service.RentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/rents")
public class RentController {
    private final CarService carService;
    private final RentService rentService;

    public RentController(CarService carService, RentService rentService) {
        this.carService = carService;
        this.rentService = rentService;
    }

    @GetMapping("/new")
    public String showRentForm(@RequestParam Integer carId,
                               Authentication authentication,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Car car;
        try {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
            if (isAdmin) {
                redirectAttributes.addFlashAttribute("errorMessage", "Doar clienții pot face rezervări.");
                return "redirect:/cars";
            }

            car = carService.getCarById(carId, false);
        } catch (CarNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/cars";
        }

        RentRequestDTO dto = new RentRequestDTO();
        dto.setCarId(carId);

        model.addAttribute("car", car);
        model.addAttribute("rentRequestDTO", dto);
        model.addAttribute("currentTime", LocalDateTime.now());
        return "rent-form";
    }

    @GetMapping("/history")
    public String history(Authentication authentication, Model model) {
        model.addAttribute("rents", rentService.getRentHistory(authentication.getName()));
        model.addAttribute("currentTime", LocalDateTime.now());
        return "rent-history";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id,
                               Authentication authentication,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            var rent = rentService.getEditableRentForClient(authentication.getName(), id);
            RentUpdateDTO dto = new RentUpdateDTO();
            dto.setRentId(rent.getId());
            dto.setStartDate(rent.getStartDate().toLocalDateTime());
            dto.setEndDate(rent.getEndDate().toLocalDateTime());
            model.addAttribute("rent", rent);
            model.addAttribute("rentUpdateDTO", dto);
            model.addAttribute("currentTime", LocalDateTime.now());
            return "rent-edit";
        } catch (BusinessRuleException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/rents/history";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateRent(@PathVariable Integer id,
                             @Valid @ModelAttribute("rentUpdateDTO") RentUpdateDTO rentUpdateDTO,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        var rent = rentService.getRentForClient(authentication.getName(), id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("rent", rent);
            model.addAttribute("currentTime", LocalDateTime.now());
            return "rent-edit";
        }

        try {
            rentUpdateDTO.setRentId(id);
            rentService.updatePendingRent(authentication.getName(), rentUpdateDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Rezervarea a fost actualizată cu succes.");
            return "redirect:/rents/history";
        } catch (BusinessRuleException ex) {
            model.addAttribute("rent", rent);
            model.addAttribute("currentTime", LocalDateTime.now());
            model.addAttribute("errorMessage", ex.getMessage());
            return "rent-edit";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelRent(@PathVariable Integer id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        try {
            rentService.cancelPendingRent(authentication.getName(), id);
            redirectAttributes.addFlashAttribute("successMessage", "Rezervarea a fost anulată cu succes.");
        } catch (BusinessRuleException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/rents/history";
    }

    @PostMapping
    public String createRent(@Valid @ModelAttribute("rentRequestDTO") RentRequestDTO rentRequestDTO,
                             BindingResult bindingResult,
                             Authentication authentication,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        Car car;
        try {
            car = carService.getCarById(rentRequestDTO.getCarId(), false);
        } catch (CarNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/cars";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("car", car);
            model.addAttribute("currentTime", LocalDateTime.now());
            return "rent-form";
        }

        try {
            rentService.createRent(rentRequestDTO, authentication.getName());
            redirectAttributes.addFlashAttribute("reservedSuccess", true);
            return "redirect:/cars/" + car.getId();
        } catch (BusinessRuleException ex) {
            model.addAttribute("car", car);
            model.addAttribute("currentTime", LocalDateTime.now());
            model.addAttribute("errorMessage", ex.getMessage());
            return "rent-form";
        } catch (CarNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/cars";
        }
    }
}

