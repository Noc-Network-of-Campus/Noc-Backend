package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.web.dto.MapResponseDto;
import com.mycompany.myapp.web.dto.PostResponseDto;

import java.util.List;

public interface MapService {
    List<MapResponseDto.MapPinDto> getPinsByCategoryAndLocation(Category category, Double latitude, Double longitude);
    List<PostResponseDto.SimplePostDto> getPinsByIds(List<Long> ids);
}
