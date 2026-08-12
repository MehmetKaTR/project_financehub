package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.response.BudgetResponse;
import com.mehmetkatr.financehub.entity.Budget;
import com.mehmetkatr.financehub.entity.Category;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.repository.BudgetRepository;
import com.mehmetkatr.financehub.repository.CategoryRepository;
import com.mehmetkatr.financehub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void createBudget_kullaniciYoksa_ResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.createBudget(
                1L, 2L, Budget.PeriodType.MONTHLY, 7, 2026, new Money(new BigDecimal("1000"), "TRY")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void createBudget_kategoriYoksa_ResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetService.createBudget(
                1L, 2L, Budget.PeriodType.MONTHLY, 7, 2026, new Money(new BigDecimal("1000"), "TRY")))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found");
    }

    @Test
    void createBudget_basarili_spendAmountSifirBaslar() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).build()));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(Category.builder().id(2L).build()));

        BudgetResponse sonuc = budgetService.createBudget(
                1L, 2L, Budget.PeriodType.MONTHLY, 7, 2026, new Money(new BigDecimal("1000"), "TRY"));

        assertThat(sonuc.getSpendAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(sonuc.getLimitAmount()).isEqualByComparingTo(new BigDecimal("1000"));
    }

    // ----- SANA BIRAKILANLAR (verify tekniği gerekiyor) -----
    @Test
    void addSpending_butceYoksa_saveCagrilmaz() {
        when(budgetRepository.findByUserIdAndCategoryIdAndYearAndMonth(1L, 2L, 2026, 7))
                .thenReturn(Optional.empty());

        budgetService.addSpending(1L, 2L, 2026, 7, new Money(new BigDecimal("500"), "TRY"));

        verify(budgetRepository, never()).save(any());
    }

    // TODO: addSpending - butce varsa spendAmount artar
    @Test
    void addSpending_butceVarsa_spendAmountArtar() {
        Budget budget = Budget.builder()
                .spendAmount(new Money(new BigDecimal("100"), "TRY"))
                .build();
        when(budgetRepository.findByUserIdAndCategoryIdAndYearAndMonth(1L, 2L, 2026, 7))
                .thenReturn(Optional.of(budget));

        budgetService.addSpending(1L, 2L, 2026, 7, new Money(new BigDecimal("500"), "TRY"));

        verify(budgetRepository).save(budget);
        assertThat(budget.getSpendAmount().getAmount()).isEqualByComparingTo(new BigDecimal("600"));
    }
}
