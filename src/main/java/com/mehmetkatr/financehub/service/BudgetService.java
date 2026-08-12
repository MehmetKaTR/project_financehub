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

    public BudgetResponse createBudget(Long userId, Long categoryId, Budget.PeriodType periodType, Integer month, Integer year, Money limitAmount){

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Budget budget = Budget.builder()
                .user(user)
                .category(category)
                .period(periodType)
                .month(month)
                .year(year)
                .limitAmount(limitAmount)
                .spendAmount(new Money(BigDecimal.ZERO, limitAmount.getCurrency())).build();

        budgetRepository.save(budget);

        return toResponse(budget);
    }

    public List<BudgetResponse> findByUserId(Long userId){

        return budgetRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public void addSpending(Long userId, Long categoryId, Integer year, Integer month, Money amount){

        Optional<Budget> findBudget = budgetRepository.findByUserIdAndCategoryIdAndYearAndMonth(userId, categoryId, year, month);

        if(findBudget.isEmpty())
            return;

        Budget budget = findBudget.get();
        budget.addSpending(amount);

        budgetRepository.save(budget);
    }

    // convert Budget to BudgetResponse
    private BudgetResponse toResponse(Budget budget) {
        BudgetResponse response = new BudgetResponse();
        response.setId(budget.getId());
        response.setUserId(budget.getUser().getId());
        if (budget.getCategory() != null) {
            response.setCategoryId(budget.getCategory().getId());
        }
        response.setPeriod(budget.getPeriod());
        response.setMonth(budget.getMonth());
        response.setYear(budget.getYear());
        response.setLimitAmount(budget.getLimitAmount().getAmount());
        response.setSpendAmount(budget.getSpendAmount().getAmount());

        return response;
    }

}
