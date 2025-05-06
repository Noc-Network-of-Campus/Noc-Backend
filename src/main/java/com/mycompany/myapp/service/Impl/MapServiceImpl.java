package com.mycompany.myapp.service.Impl;

import com.mycompany.myapp.converter.MapConverter;
import com.mycompany.myapp.converter.PostConverter;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.domain.enums.Category;
import com.mycompany.myapp.repository.PostRepository;
import com.mycompany.myapp.service.MapService;
import com.mycompany.myapp.web.dto.MapResponseDto;
import com.mycompany.myapp.web.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final PostRepository postRepository;
    private final MapConverter mapConverter;
    private final PostConverter postConverter;

    @Override
    public List<MapResponseDto.MapPinDto> getPinsByCategoryAndLocation(Category category, Double latitude, Double longitude){
        double radius = 10000.0; //미터 단위

        if (latitude == null || longitude == null)
            throw new IllegalArgumentException("위도 또는 경도가 없습니다.");

        String point = String.format("POINT(%f %f)", latitude, longitude);
        String categoryStr = category != null ? category.name() : null;

        List<Post> posts = postRepository.findByCategoryAndLocation(categoryStr, point, radius);

        return posts.stream()
                .map(mapConverter::toPin)
                .collect(Collectors.toList());
    }

    public List<PostResponseDto.SimplePostDto> getPinsByIds(List<Long> ids){
        List<Post> posts = postRepository.findAllById(ids);

        return posts.stream()
                .map(postConverter::toSimplePostDto)
                .collect(Collectors.toList());
    }
}
