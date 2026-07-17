package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.dto.request.CategoryRequest;
import com.mehmetkatr.financehub.dto.response.CategoryResponse;
import com.mehmetkatr.financehub.entity.Category;
import com.mehmetkatr.financehub.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>>  getAllCategories(){

        List<CategoryResponse> response = categoryService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable String name){
        return categoryService.findByName(name)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponse>> getCategoryByType(@PathVariable Category.CategoryType type){

        List<CategoryResponse> response = categoryService.findByType(type)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request){
            Category newCategory = categoryService.createCategory(
                    request.getName(),
                    request.getType(),
                    request.getIcon(),
                    request.getColor());

            CategoryResponse response = toResponse(newCategory);

            return  ResponseEntity.ok(response);
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
