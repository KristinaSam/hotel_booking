package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.RoomTypeDto;
import com.academy.hotel_booking.service.RoomTypeService;
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
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @GetMapping("/roomTypes")
    public String getAllRoomTypes(Model model) {
        List<RoomTypeDto> rooms = roomTypeService.findAllRoomTypes();
        model.addAttribute("roomTypes", rooms);
        return "roomTypesList";
    }

    @GetMapping("/roomTypes/{id}")
    public String getRoomTypeById(@PathVariable Integer id, Model model) {
        RoomTypeDto roomTypeDto = roomTypeService.findRoomTypeById(id);
        model.addAttribute("roomType", roomTypeDto);
        return "roomTypeDetails";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roomTypes/createRoomTypeForm")
    public String showCreateRoomTypeForm(Model model) {
        model.addAttribute("roomType", new RoomTypeDto());
        return "admin/createRoomTypeForm";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roomTypes/newRoomTypes")
    public String createRoomType(@ModelAttribute("roomType") RoomTypeDto roomTypeDto, RedirectAttributes redirectAttributes) {
        try {
            roomTypeService.saveRoomType(roomTypeDto);
            redirectAttributes.addFlashAttribute("successMessage", "Room type " + roomTypeDto.getName().toUpperCase() + " created successfully!");
            return "redirect:/roomTypes/createRoomTypeForm";
        } catch (
                RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "This room type already exists!");
            return "redirect:/roomTypes/createRoomTypeForm";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roomTypes/{id}/edit")
    public String editRoomTypeForm(@PathVariable Integer id, Model model) {
        RoomTypeDto roomTypeDto = roomTypeService.findRoomTypeById(id);
        model.addAttribute("roomType", roomTypeDto);
        return "admin/roomTypeEditForm";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roomTypes/{id}/update")
    public String updateRoomType(@PathVariable Integer id, @ModelAttribute("roomType") RoomTypeDto roomTypeDto) {
        roomTypeDto.setId(id);
        roomTypeService.updateRoomType(roomTypeDto);
        return "redirect:/roomTypes";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roomTypes/{id}/delete")
    public String confirmDeleteRoomType(@PathVariable Integer id, Model model) {
        RoomTypeDto roomTypeDto = roomTypeService.findRoomTypeById(id);
        model.addAttribute("roomType", roomTypeDto);
        return "admin/confirmDeleteRoomType";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roomTypes/{id}/delete")
    public String deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return "redirect:/roomTypes";
    }
}
