package com.mehmetkatr.financehub.dto.response;

import com.mehmetkatr.financehub.entity.Category;
import lombok.Data;

@Data
public class CategoryResponse {

    private Long id;
    private String name;
    private Category.CategoryType type;
    private String icon;
    private String color;
    private boolean isDefault;
}
