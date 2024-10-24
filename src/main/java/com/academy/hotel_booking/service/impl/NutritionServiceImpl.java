package com.academy.hotel_booking.service.impl;

import com.academy.hotel_booking.dto.NutritionDto;
import com.academy.hotel_booking.model.entity.Nutrition;
import com.academy.hotel_booking.model.repository.NutritionRepository;
import com.academy.hotel_booking.service.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionServiceImpl implements NutritionService {

    private final NutritionRepository nutritionRepository;

    @Override
    public Nutrition createNutrition(NutritionDto nutritionDto, Nutrition nutrition) {
        nutrition.setName(nutritionDto.getName());
        nutrition.setDescription(nutritionDto.getDescription());
        nutrition.setPrice(nutritionDto.getPrice());
        return nutritionRepository.save(nutrition);
    }

    @Override
    public void saveNutrition(NutritionDto nutritionDto) {
        Optional<Nutrition> existingNutrition = nutritionRepository.findByName(nutritionDto.getName());
        if (existingNutrition.isPresent()) {
            throw new RuntimeException("Nutrition already exists!");
        }
        Nutrition nutrition = new Nutrition();
        nutrition = createNutrition(nutritionDto, nutrition);
        convertToNutritionDto(nutrition);
    }

    @Override
    public void updateNutrition(NutritionDto nutritionDto) {
        Nutrition nutrition = nutritionRepository.findById(nutritionDto.getId())
                .orElseThrow(() -> new RuntimeException("Nutrition not found with id: " + nutritionDto.getId()));
        nutrition = createNutrition(nutritionDto, nutrition);
        convertToNutritionDto(nutrition);
    }

    @Override
    public void deleteNutrition(Integer id) {
        nutritionRepository.deleteById(id);
    }

    @Override
    public List<NutritionDto> findAllNutrition() {
        List<Nutrition> nutritionList = nutritionRepository.findAll();
        return nutritionList.stream().map(this::convertToNutritionDto).collect(Collectors.toList());
    }

    @Override
    public NutritionDto findNutritionById(Integer id) {
        Nutrition nutrition = nutritionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nutrition not found with id: " + id));
        return convertToNutritionDto(nutrition);
    }

    @Override
    public boolean isNutritionNameExists(String name) {
        return nutritionRepository.findByName(name).isPresent();
    }

    @Override
    public NutritionDto convertToNutritionDto(Nutrition nutrition) {
        NutritionDto nutritionDto = new NutritionDto();
        nutritionDto.setId(nutrition.getId());
        nutritionDto.setName(nutrition.getName());
        nutritionDto.setDescription(nutrition.getDescription());
        nutritionDto.setPrice(nutrition.getPrice());
        return nutritionDto;
    }

    @Override
    public Nutrition convertToNutrition(NutritionDto nutritionDto) {
        Nutrition nutrition = new Nutrition();
        nutrition.setId(nutritionDto.getId());
        nutrition.setName(nutritionDto.getName());
        nutrition.setDescription(nutritionDto.getDescription());
        nutrition.setPrice(nutritionDto.getPrice());
        return nutrition;
    }
}
