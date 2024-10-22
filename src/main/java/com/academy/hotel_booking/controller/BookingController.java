package com.academy.hotel_booking.controller;

import com.academy.hotel_booking.dto.additionalServiceDto.AdditionalServiceDto;
import com.academy.hotel_booking.dto.bookingDto.BookingDto;
import com.academy.hotel_booking.dto.customerDto.UserDto;
import com.academy.hotel_booking.dto.nutritionDto.NutritionDto;
import com.academy.hotel_booking.dto.roomDto.RoomDto;
import com.academy.hotel_booking.service.*;
import com.academy.hotel_booking.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping("/bookings/bookingForm")
//    public String showBookingForm(Model model) {
//        model.addAttribute("bookingDto", new BookingDto());
//        return "cust/bookingForm";
//   }

//    @PostMapping("/bookings/searchRooms")
//    public String searchAvailableRooms(@ModelAttribute BookingDto bookingDto, Model model) {
//        List<RoomDto> allAvailableRooms = roomService.getAvailableRooms();
//        List<RoomDto> filteredRooms = allAvailableRooms.stream()
//                .filter(room -> roomService.isRoomAvailable(room, bookingDto))
//                .collect(Collectors.toList());
//        model.addAttribute("filteredRooms", filteredRooms);
//        model.addAttribute("bookingDto", bookingDto);
//        return "customer/searchRooms";
//    }
//
//    @PostMapping("/bookings/chooseRooms")
//    public String chooseRooms(@RequestParam List<Integer> selectedRoomIds, @ModelAttribute BookingDto bookingDto, Model model) {
//        List<RoomDto> selectedRooms = roomService.getRoomsByIds(selectedRoomIds);
//        bookingDto.setRoomDtos(selectedRooms);
//        model.addAttribute("selectedRooms", selectedRooms);
//        model.addAttribute("bookingDto", bookingDto);
//
//        List<NutritionDto> nutritionList = nutritionService.findAllNutrition();
//        model.addAttribute("nutritionList", nutritionList);
//        model.addAttribute("bookingDto", bookingDto);
//        return "customer/chooseNutrition";
//    }
//
//    @PostMapping("/bookings/chooseNutrition")
//    public String chooseNutrition(@RequestParam Integer selectedNutritionId,
//                                  @ModelAttribute BookingDto bookingDto,
//                                  Model model) {
//        if (selectedNutritionId == null) {
//            System.out.println("selectedNutritionId is null");
//        } else {
//            System.out.println("selectedNutritionId: " + selectedNutritionId);
//        }
//        NutritionDto nutritionDto = nutritionService.findNutritionById(selectedNutritionId);
//        bookingDto.setNutritionDto(nutritionDto);
//
//        model.addAttribute("nutritionDto", nutritionDto);
//        model.addAttribute("bookingDto", bookingDto);
//
//        List<AdditionalServiceDto> additionalServiceList = additionalServiceService.findAllAdditionalServices();
//        model.addAttribute("additionalServiceList", additionalServiceList);
//        model.addAttribute("bookingDto", bookingDto);
//        return "customer/chooseServices";
//    }
//
//    @PostMapping("/bookings/chooseServices")
//    public String chooseServices(@RequestParam List<Integer> selectedAdditionalServiceIds, @ModelAttribute BookingDto bookingDto, Model model) {
//        List<AdditionalServiceDto> selectedAdditionalServices = additionalServiceService.findAllAdditionalServicesByIds(selectedAdditionalServiceIds);
//        bookingDto.setAdditionalServiceDtos(selectedAdditionalServices);
//        model.addAttribute("selectedAdditionalServices", selectedAdditionalServices);
//        model.addAttribute("bookingDto", bookingDto);
//
//        return "customer/allBooking";
//    }
//
//    @PostMapping("/bookings/confirmBooking")
//    public String confirmBooking(@ModelAttribute BookingDto bookingDto, Model model) {
//        // проверка выбраны ли комнаты
//        if (bookingDto.getRoomDtos() == null || bookingDto.getRoomDtos().isEmpty()) {
//            model.addAttribute("error", "Пожалуйста, выберите хотя бы одну комнату.");
//            return "customer/bookingForm";
//        }
//
//        // проверка вместимости
//        List<RoomDto> selectedRooms = bookingDto.getRoomDtos();
//        int totalCapacity = selectedRooms.stream()
//                .mapToInt(room -> room.getRoomTypeDto().getCapacity())
//                .sum();
//
//        int totalGuests = bookingDto.getAdultsCount() + bookingDto.getChildrenCount();
//        if (totalCapacity < totalGuests) {
//            model.addAttribute("error", "Выбранные комнаты не могут вместить указанное количество людей.");
//            return "customer/bookingForm";
//        }
//
//        // Установка текущего пользователя
//        UserDto currentUser = userDetailsService.getCurrentUser();
//        bookingDto.setUserDto(currentUser);
//
//        // Сохранение бронирования в базе данных
//        bookingService.saveBooking(bookingDto);
//
//        // Добавляем сообщение об успешном бронировании
//        model.addAttribute("message", "Ваше бронирование успешно подтверждено!");
//        model.addAttribute("booking", bookingDto);                                     // информация для страницы подтверждения бронирования
//
//        return "customer/confirmationBooking";
//    }

    @GetMapping("/bookings/bookingForm")
    public String showBookingForm(Model model) {
        BookingDto bookingDto = new BookingDto();
        model.addAttribute("bookingDto", bookingDto);
        System.out.println(bookingDto);
        return "customer/accord";
    }

    @PostMapping("/bookings/processBooking")
    public String processBooking(@ModelAttribute BookingDto bookingDto, Model model) {
        model.addAttribute("bookingDto", bookingDto);

        List<RoomDto> filteredRooms = roomService.getAvailableRooms().stream()
                .filter(room -> roomService.isRoomAvailable(room, bookingDto))
                .collect(Collectors.toList());
        model.addAttribute("filteredRooms", filteredRooms);
        model.addAttribute("bookingDto", bookingDto);
        if (bookingDto.getRoomDtos() == null || bookingDto.getRoomDtos().isEmpty()) {
            model.addAttribute("error", "Пожалуйста, выберите хотя бы одну комнату.");
            return "customer/accord";
        }
   //        if (filteredRooms.isEmpty()) {
//            model.addAttribute("error", "Нет доступных номеров для выбранных дат.");
//        } else {
//            model.addAttribute("filteredRooms", filteredRooms);
//
//        }
//        List<RoomDto> selectedRooms = bookingDto.getRoomDtos();
//        model.addAttribute("selectedRooms", selectedRooms);
//        model.addAttribute("bookingDto", bookingDto);

//        int totalCapacity = selectedRooms.stream().mapToInt(room -> room.getRoomTypeDto().getCapacity()).sum();
//        int totalGuests = bookingDto.getAdultsCount() + bookingDto.getChildrenCount();
//        if (totalCapacity < totalGuests) {
//            model.addAttribute("error", "Выбранные комнаты не могут вместить указанное количество людей.");
//            return "customer/accord";
//        }


        List<NutritionDto> nutritionDtoList = nutritionService.findAllNutrition();
        model.addAttribute("nutritionDtoList", nutritionDtoList);
//        NutritionDto nutritionDto = nutritionService.findNutritionById(selectedNutritionId);
//        bookingDto.setNutritionDto(nutritionDto);
//
//        model.addAttribute("nutritionDto", nutritionDto);
//        model.addAttribute("bookingDto", bookingDto);

        List<AdditionalServiceDto> additionalServiceDtoList = additionalServiceService.findAllAdditionalServices();
        model.addAttribute("additionalServiceDtoList", additionalServiceDtoList);

//        List<AdditionalServiceDto> selectedAdditionalServices = additionalServiceService.findAllAdditionalServicesByIds(selectedAdditionalServiceIds);
////        bookingDto.setAdditionalServiceDtos(selectedAdditionalServices);
//        model.addAttribute("additionalServiceList", additionalServiceService.findAllAdditionalServices());
//        model.addAttribute("bookingDto", bookingDto);

        // текущий пользователь
        UserDto currentUser = userDetailsService.getCurrentUser();
        bookingDto.setUserDto(currentUser);

        // cохранить бронирование
        bookingService.saveBooking(bookingDto);

        // сообщение об успешном бронировании
        model.addAttribute("message", "Ваше бронирование успешно подтверждено!");
        model.addAttribute("booking", bookingDto); // информация для страницы подтверждения бронирования

        return "customer/finalBooking";
    }
}
