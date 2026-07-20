package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.dto.response.CategoryResponse;
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

    public Optional<CategoryResponse> findByName(String name){
        return categoryRepository.findByName(name).map(this::toResponse);
    }

    public List<CategoryResponse> findAll(){
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<CategoryResponse> findByType(Category.CategoryType type){
        return  categoryRepository.findByType(type).stream().map(this::toResponse).toList();
    }

    public CategoryResponse createCategory(String name, Category.CategoryType type, String icon, String color){

        Category newCategory = Category.builder()
                .name(name)
                .type(type)
                .icon(icon)
                .color(color)
                .build();

        categoryRepository.save(newCategory);

        return toResponse(newCategory);
    }

    // convert Category to CategoryResponse
    private CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setType(category.getType());
        response.setIcon(category.getIcon());
        response.setColor(category.getColor());
        response.setDefault(category.isDefault());

        return response;
    }

}
