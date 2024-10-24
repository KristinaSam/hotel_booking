package com.academy.hotel_booking.service;

import com.academy.hotel_booking.dto.AdditionalServiceDto;
import com.academy.hotel_booking.model.entity.AdditionalService;

import java.util.List;

public interface AdditionalServiceService {
    AdditionalService createAdditionalService(AdditionalServiceDto additionalServiceDto, AdditionalService additionalService);

    void updateAdditionalService(AdditionalServiceDto additionalServiceDto);

    void saveAdditionalService(AdditionalServiceDto additionalServiceDto);

    void deleteAdditionalService(Integer id);

    List<AdditionalServiceDto> findAllAdditionalServices();

    AdditionalServiceDto findAdditionalServiceById(Integer id);

    List<AdditionalServiceDto> findAllAdditionalServicesByIds(List<Integer> serviceIds);

    AdditionalServiceDto convertToAdditionalServiceDto(AdditionalService additionalService);

    AdditionalService convertToAdditionalService(AdditionalServiceDto additionalServiceDto);
}
