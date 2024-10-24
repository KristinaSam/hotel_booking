package com.academy.hotel_booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutritionDto {
    private Integer id;
    private String name;
    private String description;
    private Double price;
}
