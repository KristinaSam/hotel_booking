package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.roomTypeDto.RoomTypeDto;
import com.academy.hotel_booking.service.RoomTypeService;
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
    @GetMapping("/roomTypes/createRoomTypeForm")
    public String showCreateRoomTypeForm(Model model) {
        model.addAttribute("roomType", new RoomTypeDto());
        return "createRoomTypeForm";
    }

    @PostMapping("/roomTypes/newRoomTypes")
    public String createRoomType(@ModelAttribute("roomType") RoomTypeDto roomTypeDto, RedirectAttributes redirectAttributes) {
        try {
            roomTypeService.saveRoomType(roomTypeDto);
            redirectAttributes.addFlashAttribute("successMessage", "Room type " + roomTypeDto.getName().toUpperCase() + " created successfully!");
            return "redirect:/roomTypes/createRoomTypeForm";                                                               // можно перенаправить на /roomTypes, но тогда не будет сообщения об успешном создании
        } catch (RuntimeException e) {                                                                                     // или перенаправлять на отдельную страницу с сообщением об успешном создании
            redirectAttributes.addFlashAttribute("errorMessage", "This room type already exists!");
            return "redirect:/roomTypes/createRoomTypeForm";
        }
    }

    @GetMapping("/roomTypes/{id}/edit")
    public String editRoomTypeForm(@PathVariable Integer id, Model model) {
        RoomTypeDto roomTypeDto = roomTypeService.findRoomTypeById(id);
        model.addAttribute("roomType", roomTypeDto);
        return "roomTypeEditForm";
    }

    @PostMapping("/roomTypes/{id}/update")
    public String updateRoomType(@PathVariable Integer id, @ModelAttribute("roomType") RoomTypeDto roomTypeDto) {
        roomTypeDto.setId(id);
        roomTypeService.updateRoomType(roomTypeDto);
        return "redirect:/roomTypes";
    }

    @GetMapping("/roomTypes/{id}/delete")
    public String confirmDeleteRoomType(@PathVariable Integer id, Model model) {
        RoomTypeDto roomTypeDto = roomTypeService.findRoomTypeById(id);
        model.addAttribute("roomType", roomTypeDto);
        return "confirmDeleteRoomType";
    }
    @PostMapping("/roomTypes/{id}/delete")
    public String deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return "redirect:/roomTypes";
    }
}
