package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.converter.NamecardConverter;
import com.mycompany.myapp.domain.Category;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.CategoryRepository;
import com.mycompany.myapp.repository.NamecardRepository;
import com.mycompany.myapp.service.NamecardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NamecardServiceImpl implements NamecardService {
    private final NamecardConverter namecardConverter;
    private final NamecardRepository namecardRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public void createCategory(User user, String name){
        Category category = namecardConverter.createCategory(user,name);
        categoryRepository.save(category);
    }
}
