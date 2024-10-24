package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.NutritionDto;
import com.academy.hotel_booking.service.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NutritionController {

    private final NutritionService nutritionService;

    @GetMapping("/nutrition")
    public String getAllNutrition(Model model) {
        List<NutritionDto> nutritionList = nutritionService.findAllNutrition();
        model.addAttribute("nutritionList", nutritionList);
        return "nutritionList";
    }

    @GetMapping("/nutrition/{id}")
    public String getNutritionById(@PathVariable Integer id, Model model) {
        NutritionDto nutritionDto = nutritionService.findNutritionById(id);
        model.addAttribute("nutrition", nutritionDto);
        return "nutritionDetails";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nutrition/createNutritionForm")
    public String showCreateNutritionForm(Model model) {
        model.addAttribute("nutrition", new NutritionDto());
        return "admin/createNutritionForm";
    }

    @PostMapping("/nutrition/newNutrition")
    public String createNutrition(@ModelAttribute("nutrition") NutritionDto nutritionDto, RedirectAttributes redirectAttributes) {
        try {
            nutritionService.saveNutrition(nutritionDto);
            redirectAttributes.addFlashAttribute("successMessage", "Nutrition " + nutritionDto.getName().toUpperCase() + " created successfully!");
            return "redirect:/nutrition/createNutritionForm";
        } catch (
                RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "This nutrition already exists!");
            return "redirect:/nutrition/createNutritionForm";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nutrition/{id}/edit")
    public String editNutritionForm(@PathVariable Integer id, Model model) {
        NutritionDto nutrition = nutritionService.findNutritionById(id);
        model.addAttribute("nutrition", nutrition);
        return "admin/nutritionEditForm";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nutrition/{id}/update")
    public String updateNutrition(@PathVariable Integer id, @ModelAttribute("nutrition") NutritionDto nutritionDto, Model model) {
        try {
            NutritionDto existingNutrition = nutritionService.findNutritionById(id);
            if (!existingNutrition.getName().equals(nutritionDto.getName()) &&
                    nutritionService.isNutritionNameExists(nutritionDto.getName())) {
                model.addAttribute("errorMessage", "A meal plan with that name already exists.");
                model.addAttribute("nutrition", nutritionDto);
                return "admin/nutritionEditForm";
            }
            nutritionService.updateNutrition(nutritionDto);
            model.addAttribute("successMessage", "A meal plan updated successfully.");
            return "redirect:/nutrition";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred.");
            return "admin/nutritionEditForm";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nutrition/{id}/delete")
    public String confirmDeleteNutrition(@PathVariable Integer id, Model model) {
        NutritionDto nutritionDto = nutritionService.findNutritionById(id);
        model.addAttribute("nutrition", nutritionDto);
        return "admin/confirmDeleteNutrition";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nutrition/{id}/delete")
    public String deleteNutrition(@PathVariable Integer id) {
        nutritionService.deleteNutrition(id);
        return "redirect:/nutrition";
    }
}
