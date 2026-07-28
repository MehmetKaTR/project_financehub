package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.dto.response.CategoryResponse;
import com.mehmetkatr.financehub.entity.Category;
import com.mehmetkatr.financehub.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createCategory_basarili_CategoryResponseDoner() {
        CategoryResponse sonuc = categoryService.createCategory(
                "Market", Category.CategoryType.EXPENSE, "cart", "#FF0000");

        assertThat(sonuc.getName()).isEqualTo("Market");
        assertThat(sonuc.getType()).isEqualTo(Category.CategoryType.EXPENSE);
    }

    @Test
    void findByName_kayitVarsa_CategoryResponseDoner() {
        Category category = Category.builder()
                .id(1L)
                .name("Market")
                .type(Category.CategoryType.EXPENSE)
                .build();

        when(categoryRepository.findByName("Market")).thenReturn(Optional.of(category));

        Optional<CategoryResponse> sonuc = categoryService.findByName("Market");

        assertThat(sonuc).isPresent();
        assertThat(sonuc.get().getName()).isEqualTo("Market");
    }

    @Test
    void findByName_kayitYoksa_bosOptionalDoner() {
        when(categoryRepository.findByName("Yok")).thenReturn(Optional.empty());

        Optional<CategoryResponse> sonuc = categoryService.findByName("Yok");

        assertThat(sonuc).isEmpty();
    }

    @Test
    void findAll_tumKategorileriDtoOlarakDoner() {
        Category c1 = Category.builder().id(1L).name("Market").type(Category.CategoryType.EXPENSE).build();
        Category c2 = Category.builder().id(2L).name("Maas").type(Category.CategoryType.INCOME).build();

        when(categoryRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CategoryResponse> sonuc = categoryService.findAll();

        assertThat(sonuc).hasSize(2);
        assertThat(sonuc.get(0).getName()).isEqualTo("Market");
    }
}
