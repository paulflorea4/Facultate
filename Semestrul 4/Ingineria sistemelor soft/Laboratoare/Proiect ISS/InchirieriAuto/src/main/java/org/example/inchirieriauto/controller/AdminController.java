package org.example.inchirieriauto.controller;

import jakarta.validation.Valid;
import org.example.inchirieriauto.dto.CarFormDTO;
import org.example.inchirieriauto.dto.FeatureFormDTO;
import org.example.inchirieriauto.exception.BusinessRuleException;
import org.example.inchirieriauto.exception.CarNotFoundException;
import org.example.inchirieriauto.model.CarStatus;
import org.example.inchirieriauto.service.AdminCarService;
import org.example.inchirieriauto.service.FeatureService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminCarService adminCarService;
    private final FeatureService featureService;

    public AdminController(AdminCarService adminCarService, FeatureService featureService) {
        this.adminCarService = adminCarService;
        this.featureService = featureService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("cars", adminCarService.getDashboardCars());
        model.addAttribute("features", featureService.getAllFeatures());
        return "admin-dashboard";
    }

    @GetMapping("/cars/new")
    public String showCarForm(Model model) {
        model.addAttribute("carFormDTO", new CarFormDTO());
        model.addAttribute("features", featureService.getAllFeatures());
        model.addAttribute("statuses", Arrays.stream(CarStatus.values()).toList());
        return "admin-car-form";
    }

     @PostMapping("/cars")
     public String saveCar(@Valid @ModelAttribute("carFormDTO") CarFormDTO carFormDTO,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
         if (bindingResult.hasErrors()) {
             model.addAttribute("features", featureService.getAllFeatures());
             model.addAttribute("statuses", Arrays.stream(CarStatus.values()).toList());
             return "admin-car-form";
         }

         try {
             adminCarService.saveCar(carFormDTO);
             redirectAttributes.addFlashAttribute("successMessage", "Mașina a fost salvată cu succes.");
             return "redirect:/admin";
         } catch (BusinessRuleException ex) {
             model.addAttribute("features", featureService.getAllFeatures());
             model.addAttribute("statuses", Arrays.stream(CarStatus.values()).toList());
             model.addAttribute("errorMessage", ex.getMessage());
             return "admin-car-form";
         }
     }

    @GetMapping("/cars/{id}/edit")
    public String editCar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("carFormDTO", adminCarService.getCarForm(id));
            model.addAttribute("features", featureService.getAllFeatures());
            model.addAttribute("statuses", Arrays.stream(CarStatus.values()).toList());
            return "admin-car-form";
        } catch (CarNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin";
        }
    }

    @PostMapping("/cars/{id}/delete")
    public String deleteCar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            adminCarService.deleteCar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Mașina a fost ștearsă cu succes.");
        } catch (CarNotFoundException | BusinessRuleException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin";
    }

    @GetMapping("/features/new")
    public String showFeatureForm(Model model) {
        model.addAttribute("featureFormDTO", new FeatureFormDTO());
        return "admin-feature-form";
    }

    @PostMapping("/features")
    public String saveFeature(@Valid @ModelAttribute("featureFormDTO") FeatureFormDTO featureFormDTO,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin-feature-form";
        }

        try {
            featureService.saveFeature(featureFormDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Dotarea a fost salvată cu succes.");
            return "redirect:/admin";
        } catch (BusinessRuleException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "admin-feature-form";
        }
    }

    @GetMapping("/features/{id}/edit")
    public String editFeature(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            var feature = featureService.getFeature(id);
            FeatureFormDTO dto = new FeatureFormDTO();
            dto.setId(feature.getId());
            dto.setName(feature.getName());
            model.addAttribute("featureFormDTO", dto);
            return "admin-feature-form";
        } catch (BusinessRuleException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/admin";
        }
    }

    @PostMapping("/features/{id}/delete")
    public String deleteFeature(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            featureService.deleteFeature(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dotarea a fost ștearsă cu succes.");
        } catch (BusinessRuleException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (DataAccessException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nu putem șterge dotarea momentan. Baza de date nu răspunde.");
        }
        return "redirect:/admin";
    }
}

