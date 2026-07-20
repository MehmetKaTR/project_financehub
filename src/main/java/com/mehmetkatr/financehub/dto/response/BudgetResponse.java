package com.mehmetkatr.financehub.dto.response;

import com.mehmetkatr.financehub.entity.Budget;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetResponse {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Budget.PeriodType period;
    private Integer month;
    private Integer year;
    private BigDecimal limitAmount;
    private BigDecimal spendAmount;
}
