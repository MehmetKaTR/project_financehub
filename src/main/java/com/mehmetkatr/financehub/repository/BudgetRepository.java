package com.mehmetkatr.financehub.repository;

import com.mehmetkatr.financehub.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserId(Long id);

    List<Budget> findByCategoryId(Long id);

    List<Budget> findByPeriod(Budget.PeriodType periodType);

    List<Budget> findByYear(Integer year);

    List<Budget> findByYearAndMonth(Integer year, Integer month);
}
