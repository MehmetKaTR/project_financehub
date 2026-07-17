package com.mehmetkatr.financehub.dto.response;

import com.mehmetkatr.financehub.entity.Category;
import lombok.Data;

@Data
public class CategoryResponse {

    public Long id;
    public String name;
    public Category.CategoryType type;
    public String icon;
    public String color;
    public boolean isDefault;
}
