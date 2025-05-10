package com.mycompany.myapp.converter;

import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.web.dto.MapResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MapConverter {

    /**
     * 게시글(Post) 정보를 기반으로 지도에 사용할 핀 DTO로 변환
     *
     * @param post 게시글 엔티티
     * @return MapPinDto (지도에 표시할 핀 정보)
     */
    public MapResponseDto.MapPinDto toPin(Post post){
        return MapResponseDto.MapPinDto.builder()
                .pinId(post.getId())
                .category(post.getCategory().name())
                .latitude(post.getLocation().getY())
                .longitude(post.getLocation().getX())
                .build();
    }
}
