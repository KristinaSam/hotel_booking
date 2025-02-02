package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.*;
import com.academy.hotel_booking.model.entity.Booking;
import com.academy.hotel_booking.service.*;
import com.academy.hotel_booking.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookingController {
    private final RoomTypeService roomTypeService;
    private final UserDetailsServiceImpl userDetailsService;

    private final BookingService bookingService;
    private final AdditionalServiceService additionalServiceService;
    private final RoomService roomService;
    private final NutritionService nutritionService;


    @GetMapping("/bookings")
    public String getAllBookings(Model model) {

        List<BookingDto> bookings = bookingService.findAllBookings();
        model.addAttribute("bookings", bookings);
        return "bookingsList";
    }

    @GetMapping("/bookings/{id}")
    public String getBookingById(@PathVariable Integer id, Model model) {
        BookingDto booking = bookingService.findBookingById(id);
        model.addAttribute("booking", booking);
        return "bookingDetails";
    }

    @GetMapping("/bookings/bookingForm")
    public String showBookingForm(Model model) {
        BookingDto bookingDto = new BookingDto();
        model.addAttribute("bookingDto", bookingDto);
        return "customer/bookingForm";
    }

    @PostMapping("/bookings/searchRooms")
    public String chooseRooms(@ModelAttribute BookingDto bookingDto, Model model) {

        Booking savedBooking = bookingService.creationBooking(bookingDto);
        bookingDto.setId(savedBooking.getId());
        model.addAttribute("bookingDto", bookingDto);
        System.out.println(bookingDto);

        List<RoomDto> allAvailableRooms = roomService.getAvailableRooms();
        List<RoomDto> filteredRooms = allAvailableRooms.stream()
                .filter(room -> roomService.isRoomAvailable(room, bookingDto))
                .collect(Collectors.toList());
        model.addAttribute("filteredRooms", filteredRooms);
        model.addAttribute("bookingDto", bookingDto);
        System.out.println(bookingDto);
        return "customer/chooseRooms";
    }


    @PostMapping("/bookings/chooseRooms")
    public String chooseNutrition(@RequestParam List<Integer> selectedRoomIds,
                                  @ModelAttribute BookingDto bookingDto,
                                  Model model) {
        System.out.println(bookingDto.getId());

        bookingDto.setRoomDtos(roomService.getRoomsByIds(selectedRoomIds));
        bookingService.addSelectedRoomsToBooking(bookingDto, selectedRoomIds);

        List<NutritionDto> nutritionList = nutritionService.findAllNutrition();
        model.addAttribute("nutritionList", nutritionList);
        model.addAttribute("bookingDto", bookingDto);
        return "customer/chooseNutrition";
    }

    @PostMapping("/bookings/chooseNutrition")
    public String chooseNutrition(@RequestParam Integer selectedNutritionId,
                                  @ModelAttribute BookingDto bookingDto,
                                  Model model) {
        bookingDto.setNutritionDto(nutritionService.findNutritionById(selectedNutritionId));

        bookingService.addSelectedNutritionToBooking(bookingDto, selectedNutritionId);
        model.addAttribute("bookingDto", bookingDto);
        System.out.println(bookingDto);

        List<AdditionalServiceDto> additionalServiceList = additionalServiceService.findAllAdditionalServices();
        model.addAttribute("additionalServiceList", additionalServiceList);
        model.addAttribute("bookingDto", bookingDto);
        return "customer/chooseServices";
    }

    @PostMapping("/bookings/chooseServices")
    public String chooseServices(@RequestParam List<Integer> selectedAdditionalServiceIds,
                                 @ModelAttribute BookingDto bookingDto,
                                 RedirectAttributes redirectAttributes) {
        bookingDto.setAdditionalServiceDtos(additionalServiceService.findAllAdditionalServicesByIds(selectedAdditionalServiceIds));
        bookingService.addSelectedServicesToBooking(bookingDto, selectedAdditionalServiceIds);
        redirectAttributes.addFlashAttribute("bookingDto", bookingDto);
        return "redirect:/bookings/confirm/" + bookingDto.getId();
    }

    @GetMapping("/bookings/confirm/{id}")
    public String showConfirmationPage(@PathVariable Integer id, Model model) {
        BookingDto bookingDto = bookingService.findBookingById(id);
        model.addAttribute("bookingDto", bookingDto);
        return "customer/confirmationBooking";
    }

    @PostMapping("/bookings/confirm/{id}")
    public String confirmBooking(@ModelAttribute BookingDto bookingDto, Model model) {
        bookingService.confirmBooking(bookingDto);
        model.addAttribute("message", "Your booking has been successfully confirmed!");
        return "redirect:/user/bookings";
    }

    @PostMapping("/bookings/cancel/{id}")
    public String cancelBooking(@ModelAttribute BookingDto bookingDto, Model model) {
        bookingService.cancelBooking(bookingDto);
        model.addAttribute("message", "Your booking has been successfully canceled.");
        return "redirect:/home";                                                                                             //продумать куда перенаправить
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/bookings/{id}/delete")
    public String deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return "redirect:/home";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bookings/{id}/adminDelete")
    public String adminConfirmDeleteBooking(@PathVariable Integer id, Model model) {
        BookingDto bookingDto = bookingService.findBookingById(id);
        model.addAttribute("booking", bookingDto);
        return "admin/confirmDeleteBooking";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/bookings/{id}/delete")
    public String adminDeleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return "redirect:/bookings";
    }

    @GetMapping("/user/bookings")
    public String viewUserBookings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<BookingDto> userBookings = bookingService.getBookingsByUsername(username);
        model.addAttribute("userBookings", userBookings);
        return "customer/userBookings"; // Имя вашего шаблона
    }
}
