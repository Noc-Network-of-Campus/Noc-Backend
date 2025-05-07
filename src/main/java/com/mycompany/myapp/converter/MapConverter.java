package com.mycompany.myapp.converter;

import com.mycompany.myapp.domain.Comment;
import com.mycompany.myapp.domain.Member;
import com.mycompany.myapp.domain.Post;
import com.mycompany.myapp.web.dto.MapResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MapConverter {

    public MapResponseDto.MapPinDto toPin(Post post){
        return MapResponseDto.MapPinDto.builder()
                .pinId(post.getId())
                .category(post.getCategory().name())
                .latitude(post.getLocation().getY())
                .longitude(post.getLocation().getX())
                .build();
    }
}
