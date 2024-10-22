package com.academy.hotel_booking.dto.additionalServiceDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalServiceDto {

    private Integer id;
    private String name;
    private String description;
    private Double price;
}
