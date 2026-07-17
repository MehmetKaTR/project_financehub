package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.entity.Budget;
import com.mehmetkatr.financehub.entity.Category;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.repository.BudgetRepository;
import com.mehmetkatr.financehub.repository.CategoryRepository;
import com.mehmetkatr.financehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Budget createBudget(Long userId, Long categoryId, Budget.PeriodType periodType, Integer month, Integer year, BigDecimal limitAmount){

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Budget budget = Budget.builder()
                .user(user)
                .category(category)
                .period(periodType)
                .month(month)
                .year(year)
                .limitAmount(limitAmount)
                .spendAmount(BigDecimal.ZERO).build();

        return budgetRepository.save(budget);
    }

    public List<Budget> findByUserId(Long userId){

        return budgetRepository.findByUserId(userId);
    }

    public void addSpending(Long userId, Long categoryId, Integer year, Integer month, BigDecimal amount){

        Optional<Budget> findBudget = budgetRepository.findByUserIdAndCategoryIdAndYearAndMonth(userId, categoryId, year, month);

        if(findBudget.isEmpty())
            return;

        Budget budget = findBudget.get();
        budget.setSpendAmount(budget.getSpendAmount().add(amount));

        budgetRepository.save(budget);
    }


}
