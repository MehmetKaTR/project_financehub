package com.mehmetkatr.financehub.dto;

import com.mehmetkatr.financehub.entity.Category;
import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private Category.CategoryType type;
    private String icon;
    private String color;
}
