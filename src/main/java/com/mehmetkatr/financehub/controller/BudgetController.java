package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.request.BudgetRequest;
import com.mehmetkatr.financehub.dto.response.BudgetResponse;
import com.mehmetkatr.financehub.entity.Budget;
import com.mehmetkatr.financehub.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(@RequestBody BudgetRequest request){

        BudgetResponse newBudget = budgetService.createBudget(
                request.getUserId(),
                request.getCategoryId(),
                request.getPeriodType(),
                request.getMonth(),
                request.getYear(),
                new Money(request.getLimitAmount(), request.getCurrency())
        );

        return ResponseEntity.ok(newBudget);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetResponse>> findByUserId(@PathVariable Long userId){

        List<BudgetResponse> budget = budgetService.findByUserId(userId);

        return ResponseEntity.ok(budget);
    }


}
