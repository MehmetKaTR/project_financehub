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

        List<CategoryResponse> response = categoryService.findAll();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryResponse> getCategoryByName(@PathVariable String name){
        return categoryService.findByName(name)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponse>> getCategoryByType(@PathVariable Category.CategoryType type){

        List<CategoryResponse> response = categoryService.findByType(type);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request){
            CategoryResponse newCategory = categoryService.createCategory(
                    request.getName(),
                    request.getType(),
                    request.getIcon(),
                    request.getColor());

            return  ResponseEntity.ok(newCategory);
    }
}
