package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.additionalServiceDto.AdditionalServiceDto;
import com.academy.hotel_booking.service.AdditionalServiceService;
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
    @GetMapping("/additionalServices/createAdditionalServiceForm")
    public String showCreateAdditionalServiceForm(Model model) {
        model.addAttribute("additionalService", new AdditionalServiceDto());
        return "createAdditionalServiceForm";
    }

    @PostMapping("/additionalServices/newAdditionalService")
    public String createAdditionalService(@ModelAttribute("additionalService") AdditionalServiceDto additionalServiceDto, RedirectAttributes redirectAttributes) {
        try {
            additionalServiceService.saveAdditionalService(additionalServiceDto);
            redirectAttributes.addFlashAttribute("successMessage", "Additional service " + additionalServiceDto.getName().toUpperCase() + " created successfully!");
            return "redirect:/additionalServices/createAdditionalServiceForm";                                                               // можно перенаправить на /additionalServices, но тогда не будет сообщения об успешном создании
        } catch (RuntimeException e) {                                                                                     // или перенаправлять на отдельную страницу с сообщением об успешном создании
            redirectAttributes.addFlashAttribute("errorMessage", "This additional service already exists!");
            return "redirect:/additionalServices/createAdditionalServiceForm";
        }
    }

    @GetMapping("/additionalServices/{id}/edit")
    public String editAdditionalServiceForm(@PathVariable Integer id, Model model) {
        AdditionalServiceDto additionalService = additionalServiceService.findAdditionalServiceById(id);
        model.addAttribute("additionalService", additionalService);
        return "additionalServiceEditForm";
    }

    @PostMapping("/additionalServices/{id}/update")
    public String updateAdditionalService(@PathVariable Integer id, @ModelAttribute("additionalService") AdditionalServiceDto additionalServiceDto) {
        additionalServiceDto.setId(id);
        additionalServiceService.updateAdditionalService(additionalServiceDto);
        return "redirect:/additionalServices";
    }

    @GetMapping("/additionalServices/{id}/delete")
    public String confirmDeleteAdditionalService(@PathVariable Integer id, Model model) {
        AdditionalServiceDto additionalService = additionalServiceService.findAdditionalServiceById(id);
        model.addAttribute("additionalService", additionalService);
        return "confirmDeleteAdditionalServiceForm";
    }
    @PostMapping("/additionalServices/{id}/delete")
    public String deleteAdditionalService(@PathVariable Integer id) {
        additionalServiceService.deleteAdditionalService(id);
        return "redirect:/nutrition";
    }

}
