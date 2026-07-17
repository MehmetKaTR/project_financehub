package com.mehmetkatr.financehub.dto.request;

import com.mehmetkatr.financehub.entity.Budget;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetRequest {

    private Long userId;
    private Long categoryId;
    private Budget.PeriodType periodType;
    private Integer month;
    private Integer year;
    private BigDecimal limitAmount;
}
