package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.dto.CategoryRequest;
import com.mehmetkatr.financehub.entity.Category;
import com.mehmetkatr.financehub.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>>  getAllCategories(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name){
        Optional<Category> category = categoryService.findByName(name);

        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Category>> getCategoryByType(@PathVariable Category.CategoryType type){
        return ResponseEntity.ok(categoryService.findByType(type));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request){
            Category newCategory = categoryService.createCategory(
                    request.getName(),
                    request.getType(),
                    request.getIcon(),
                    request.getColor());

            return  ResponseEntity.ok(newCategory);
    }
}
