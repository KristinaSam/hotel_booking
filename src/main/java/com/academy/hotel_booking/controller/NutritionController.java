package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.nutritionDto.NutritionDto;
import com.academy.hotel_booking.model.entity.Nutrition;
import com.academy.hotel_booking.service.NutritionService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/nutrition/createNutritionForm")
    public String showCreateNutritionForm(Model model) {
        model.addAttribute("nutrition", new NutritionDto());
        return "createNutritionForm";
    }

    @PostMapping("/nutrition/newNutrition")
    public String createNutrition(@ModelAttribute("nutrition") NutritionDto nutritionDto, RedirectAttributes redirectAttributes) {
        try {
            nutritionService.saveNutrition(nutritionDto);
            redirectAttributes.addFlashAttribute("successMessage", "Nutrition " + nutritionDto.getName().toUpperCase() + " created successfully!");
            return "redirect:/nutrition/createNutritionForm";                                                               // можно перенаправить на /nutrition, но тогда не будет сообщения об успешном создании
        } catch (RuntimeException e) {                                                                                     // или перенаправлять на отдельную страницу с сообщением об успешном создании
            redirectAttributes.addFlashAttribute("errorMessage", "This nutrition already exists!");
            return "redirect:/nutrition/createNutritionForm";
        }
    }

    @GetMapping("/nutrition/{id}/edit")
    public String editNutritionForm(@PathVariable Integer id, Model model) {
        NutritionDto nutrition = nutritionService.findNutritionById(id);
        model.addAttribute("nutrition", nutrition);
        return "nutritionEditForm";
    }

    @PostMapping("/nutrition/{id}/update")
    public String updateNutrition(@PathVariable Integer id, @ModelAttribute("nutrition") NutritionDto nutritionDto, Model model) {
        try {
             NutritionDto existingNutrition = nutritionService.findNutritionById(id);
            if (!existingNutrition.getName().equals(nutritionDto.getName()) &&
                    nutritionService.isNutritionNameExists(nutritionDto.getName())) {
                model.addAttribute("errorMessage", "A meal plan with that name already exists.");
                model.addAttribute("nutrition", nutritionDto);
                return "nutritionEditForm";
            }
            nutritionService.updateNutrition(nutritionDto);
            model.addAttribute("successMessage", "A meal plan updated successfully.");
            return "redirect:/nutrition";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred.");
            return "nutritionEditForm";
        }
    }

    @GetMapping("/nutrition/{id}/delete")
    public String confirmDeleteNutrition(@PathVariable Integer id, Model model) {
        NutritionDto nutritionDto = nutritionService.findNutritionById(id);
        model.addAttribute("nutrition", nutritionDto);
        return "confirmDeleteNutrition";
    }

    @PostMapping("/nutrition/{id}/delete")
    public String deleteNutrition(@PathVariable Integer id) {
        nutritionService.deleteNutrition(id);
        return "redirect:/nutrition";
    }
}
