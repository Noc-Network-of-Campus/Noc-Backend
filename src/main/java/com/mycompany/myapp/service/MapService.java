package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.web.dto.MapResponseDto;

import java.util.List;

public interface MapService {
    List<MapResponseDto.MapPinDto> getPinsByCategoryAndLocation(Category category, Double latitude, Double longitude);
}
