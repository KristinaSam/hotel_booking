package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.customerDto.UserDto;
import com.academy.hotel_booking.dto.customerDto.UserRegistrationDto;
import com.academy.hotel_booking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute ("user") UserRegistrationDto userRegistrationDto, Model model) {
        try {
            userService.registerUser(userRegistrationDto);
            } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            return "register";
        }
        model.addAttribute("message", "Welcome to our Hotel Booking System!");
        return "success";
    }
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {
        String username = principal.getName();
        UserDto user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "dashboard";
    }

    @GetMapping("/dashboard/{userId}")
    public String getUserProfile(@PathVariable Integer userId, Model model) {
        UserDto userDto = userService.findId(userId);
        if (userDto == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        model.addAttribute("user", userDto);
        return "dashboard";
    }

//    @GetMapping("/dashboard/edit")
//    public String editProfile(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return "redirect:/login";
//        }
//
//        CustomerDto customer = userService.mapToCustomerDto((User) authentication.getPrincipal());
//
//        model.addAttribute("customer", customer);
//        return "editDashboard";
//    }

    @PostMapping("/dashboard/edit")
    public String updateProfile(@ModelAttribute("user") UserDto userDto) {
        if (userDto.getId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        userService.updateUser(userDto);
        return "redirect:/dashboard" + userDto.getId();
    }

    @GetMapping(value = "/users")
    public String getUsers(Model model) {
        List<UserDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "usersList";
    }
    @GetMapping(value = "/user")
    public String getUser(@RequestParam Integer id, Model model) {
        UserDto user = userService.findId(id);
        model.addAttribute("user", user);
        return "userDetails";

    }

}
