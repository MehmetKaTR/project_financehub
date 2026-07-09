package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.entity.Category;
import com.mehmetkatr.financehub.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Category> findByName(String name){
        return categoryRepository.findByName(name);
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public List<Category> findByType(Category.CategoryType type){
        return  categoryRepository.findByType(type);
    }

    public Category createCategory(String name, Category.CategoryType type, String icon, String color){

        Category newCategory = Category.builder()
                .name(name)
                .type(type)
                .icon(icon)
                .color(color)
                .build();

        return categoryRepository.save(newCategory);
    }

}
