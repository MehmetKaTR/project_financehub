package com.mehmetkatr.financehub.repository;

import com.mehmetkatr.financehub.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    Optional<Category> findByType(Category.CategoryType type);

}
