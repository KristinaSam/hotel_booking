package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.roomDto.RoomDto;
import com.academy.hotel_booking.dto.roomTypeDto.RoomTypeDto;
import com.academy.hotel_booking.service.RoomService;
import com.academy.hotel_booking.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomTypeService roomTypeService;

    @GetMapping("/rooms")
    public String getAllRooms(Model model) {
        List<RoomDto> rooms = roomService.findAllRooms();
        model.addAttribute("rooms", rooms);
        return "roomsList";
    }
    @GetMapping("/rooms/{id}")
    public String getRoomById(@PathVariable Integer id, Model model) {
        RoomDto roomDto = roomService.findRoomById(id);
        model.addAttribute("room", roomDto);
        return "roomDetails";
    }

    @GetMapping("rooms/createRoomForm")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("room", new RoomDto());
        model.addAttribute("roomTypes", roomTypeService.findAllRoomTypes());
        return "createRoomForm";
    }

    @PostMapping("/rooms/newRooms")
    public String createRoom(@ModelAttribute("room") RoomDto roomDto, RedirectAttributes redirectAttributes) {
        if (roomService.isRoomNumberExists(roomDto.getNumber())) {
            redirectAttributes.addFlashAttribute("errorMessage", "A room with this number already exists!");
            return "redirect:/rooms/createRoomForm";
        }
        roomService.saveRoom(roomDto);
        return "redirect:/rooms";
    }

    @GetMapping("/rooms/{id}/edit")
    public String editRoomForm(@PathVariable Integer id, Model model) {
        RoomDto roomDto = roomService.findRoomById(id);
        model.addAttribute("room", roomDto);
        List<RoomTypeDto> roomTypes = roomTypeService.findAllRoomTypes();
        model.addAttribute("roomTypes", roomTypes);
        return "roomEditForm";
    }

    @PostMapping("/rooms/{id}/update")
    public String updateRoom(@PathVariable Integer id, @ModelAttribute("room") RoomDto roomDto) {
        roomDto.setId(id);
        roomService.updateRoom(roomDto);
        return "redirect:/rooms";
    }

    @GetMapping("/rooms/{id}/delete")
    public String confirmDeleteRoom(@PathVariable Integer id, Model model) {
        RoomDto roomDto = roomService.findRoomById(id);
        model.addAttribute("room", roomDto);
        return "confirmDeleteRoom";
    }
    @PostMapping("/rooms/{id}/delete")
    public String deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms";
    }

    @GetMapping("/rooms/available")
    public String getAvailableRooms(Model model) {
        List<RoomDto> availableRooms = roomService.getAvailableRooms();
        model.addAttribute("availableRooms", availableRooms);
        return "availableRoomsList";
    }

    @GetMapping("/rooms/unavailable")
    public String getUnavailableRooms(Model model) {
        List<RoomDto> unavailableRooms = roomService.getUnavailableRooms();
        model.addAttribute("unavailableRooms", unavailableRooms);
        return "unavailableRoomsList";
    }

}
