package com.academy.hotel_booking.service;

import com.academy.hotel_booking.dto.nutritionDto.NutritionDto;
import com.academy.hotel_booking.model.entity.Nutrition;

import java.util.List;

public interface NutritionService {
    Nutrition createNutrition(NutritionDto nutritionDto, Nutrition nutrition);
    void updateNutrition(NutritionDto nutritionDto);
    void saveNutrition(NutritionDto nutritionDto);
    void deleteNutrition(Integer id);
    List<NutritionDto> findAllNutrition();
    NutritionDto findNutritionById(Integer id);
    NutritionDto convertToNutritionDto(Nutrition nutrition);
    boolean isNutritionNameExists(String name);
}
