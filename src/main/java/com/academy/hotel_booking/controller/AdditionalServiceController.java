package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.AdditionalServiceDto;
import com.academy.hotel_booking.service.AdditionalServiceService;
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
public class AdditionalServiceController {
    private final AdditionalServiceService additionalServiceService;

    @GetMapping("/additionalServices")
    public String getAllAdditionalServices(Model model) {
        List<AdditionalServiceDto> additionalServices = additionalServiceService.findAllAdditionalServices();
        model.addAttribute("additionalServices", additionalServices);
        return "additionalServiceList";
    }

    @GetMapping("/additionalServices/{id}")
    public String getAdditionalServiceById(@PathVariable Integer id, Model model) {
        AdditionalServiceDto additionalService = additionalServiceService.findAdditionalServiceById(id);
        model.addAttribute("additionalService", additionalService);
        return "additionalServiceDetails";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/additionalServices/createAdditionalServiceForm")
    public String showCreateAdditionalServiceForm(Model model) {
        model.addAttribute("additionalService", new AdditionalServiceDto());
        return "admin/createAdditionalServiceForm";
    }

    @PostMapping("/additionalServices/newAdditionalService")
    public String createAdditionalService(@ModelAttribute("additionalService") AdditionalServiceDto additionalServiceDto, RedirectAttributes redirectAttributes) {
        try {
            additionalServiceService.saveAdditionalService(additionalServiceDto);
            redirectAttributes.addFlashAttribute("successMessage", "Additional service " + additionalServiceDto.getName().toUpperCase() + " created successfully!");
            return "redirect:/additionalServices/createAdditionalServiceForm";
        } catch (
                RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "This additional service already exists!");
            return "redirect:/additionalServices/createAdditionalServiceForm";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/additionalServices/{id}/edit")
    public String editAdditionalServiceForm(@PathVariable Integer id, Model model) {
        AdditionalServiceDto additionalService = additionalServiceService.findAdditionalServiceById(id);
        model.addAttribute("additionalService", additionalService);
        return "admin/additionalServiceEditForm";
    }

    @PostMapping("/additionalServices/{id}/update")
    public String updateAdditionalService(@PathVariable Integer id, @ModelAttribute("additionalService") AdditionalServiceDto additionalServiceDto) {
        additionalServiceDto.setId(id);
        additionalServiceService.updateAdditionalService(additionalServiceDto);
        return "redirect:/additionalServices";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/additionalServices/{id}/delete")
    public String confirmDeleteAdditionalService(@PathVariable Integer id, Model model) {
        AdditionalServiceDto additionalService = additionalServiceService.findAdditionalServiceById(id);
        model.addAttribute("additionalService", additionalService);
        return "admin/confirmDeleteAdditionalServiceForm";
    }

    @PostMapping("/additionalServices/{id}/delete")
    public String deleteAdditionalService(@PathVariable Integer id) {
        additionalServiceService.deleteAdditionalService(id);
        return "redirect:/nutrition";
    }

}
