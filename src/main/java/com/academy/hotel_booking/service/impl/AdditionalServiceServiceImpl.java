package com.academy.hotel_booking.service.impl;

import com.academy.hotel_booking.dto.AdditionalServiceDto;
import com.academy.hotel_booking.model.entity.AdditionalService;
import com.academy.hotel_booking.model.repository.AdditionalServiceRepository;
import com.academy.hotel_booking.service.AdditionalServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdditionalServiceServiceImpl implements AdditionalServiceService {

    private final AdditionalServiceRepository additionalServiceRepository;

    @Override
    public void saveAdditionalService(AdditionalServiceDto additionalServiceDto) {
        Optional<AdditionalService> existingAdditionalService = additionalServiceRepository.findByName(additionalServiceDto.getName());
        if (existingAdditionalService.isPresent()) {
            throw new RuntimeException("Additional Service already exists!");
        }
        AdditionalService additionalService = new AdditionalService();
        additionalService = createAdditionalService(additionalServiceDto, additionalService);
        convertToAdditionalServiceDto(additionalService);
    }

    @Override
    public void updateAdditionalService(AdditionalServiceDto additionalServiceDto) {
        AdditionalService additionalService = additionalServiceRepository.findById(additionalServiceDto.getId())
                .orElseThrow(() -> new RuntimeException("Additional Service not found with id: " + additionalServiceDto.getId()));
        additionalService = createAdditionalService(additionalServiceDto, additionalService);
        convertToAdditionalServiceDto(additionalService);
    }

    @Override
    public void deleteAdditionalService(Integer id) {
        additionalServiceRepository.deleteById(id);
    }

    @Override
    public List<AdditionalServiceDto> findAllAdditionalServices() {
        List<AdditionalService> additionalServices = additionalServiceRepository.findAll();
        return additionalServices.stream().map(this::convertToAdditionalServiceDto).collect(Collectors.toList());
    }

    @Override
    public AdditionalServiceDto findAdditionalServiceById(Integer id) {
        AdditionalService additionalService = additionalServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Additional Service not found with id: " + id));
        return convertToAdditionalServiceDto(additionalService);
    }

    @Override
    public List<AdditionalServiceDto> findAllAdditionalServicesByIds(List<Integer> serviceIds) {
        List<AdditionalService> additionalServices = additionalServiceRepository.findAllById(serviceIds);
        return additionalServices.stream().map(this::convertToAdditionalServiceDto).collect(Collectors.toList());
    }

    @Override
    public AdditionalServiceDto convertToAdditionalServiceDto(AdditionalService additionalService) {
        AdditionalServiceDto additionalServiceDto = new AdditionalServiceDto();
        additionalServiceDto.setId(additionalService.getId());
        additionalServiceDto.setName(additionalService.getName());
        additionalServiceDto.setDescription(additionalService.getDescription());
        additionalServiceDto.setPrice(additionalService.getPrice());
        return additionalServiceDto;
    }

    @Override
    public AdditionalService convertToAdditionalService(AdditionalServiceDto additionalServiceDto) {
        AdditionalService additionalService = new AdditionalService();
        additionalService.setId(additionalServiceDto.getId());
        additionalService.setName(additionalServiceDto.getName());
        additionalService.setDescription(additionalServiceDto.getDescription());
        additionalService.setPrice(additionalServiceDto.getPrice());
        return additionalService;
    }

    @Override
    public AdditionalService createAdditionalService(AdditionalServiceDto additionalServiceDto, AdditionalService additionalService) {
        additionalService.setName(additionalServiceDto.getName());
        additionalService.setDescription(additionalServiceDto.getDescription());
        additionalService.setPrice(additionalServiceDto.getPrice());
        return additionalServiceRepository.save(additionalService);
    }
}
