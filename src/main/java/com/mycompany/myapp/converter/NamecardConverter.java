package com.mycompany.myapp.converter;

import com.mycompany.myapp.domain.Category;
import com.mycompany.myapp.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NamecardConverter {

    public Category createCategory(User user, String name){
        return Category.builder()
                .user(user)
                .name(name)
                .build();
    }
}
