package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.Category;
import com.mehmetkatr.financehub.entity.Transaction;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.CategoryRepository;
import com.mehmetkatr.financehub.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void createTransaction_hesapYoksa_ResourceNotFoundException() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createTransaction(
                1L, 2L, new BigDecimal("100"), "TRY", Transaction.TransactionTypes.EXPENSE, "market"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Bank Account not found");
    }

    @Test
    void createTransaction_kategoriYoksa_ResourceNotFoundException() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(BankAccount.builder().build()));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.createTransaction(
                1L, 2L, new BigDecimal("100"), "TRY", Transaction.TransactionTypes.EXPENSE, "market"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found");
    }

    @Test
    void createTransaction_EXPENSE_addSpendingCagrilir() {
        User user = User.builder().id(5L).build();
        BankAccount account = BankAccount.builder().id(1L).user(user).build();
        Category category = Category.builder().id(2L).build();

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        transactionService.createTransaction(
                1L, 2L, new BigDecimal("100"), "TRY", Transaction.TransactionTypes.EXPENSE, "market");

        // EXPENSE oldugu icin budgetService.addSpending cagrilmali
        verify(budgetService).addSpending(eq(5L), eq(2L), any(), any(), eq(new BigDecimal("100")));
    }

    @Test
    void createTransaction_INCOME_addSpendingCagrilmaz() {
        BankAccount account = BankAccount.builder().id(1L).user(User.builder().id(5L).build()).build();
        Category category = Category.builder().id(2L).build();

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        transactionService.createTransaction(
                1L, 2L, new BigDecimal("100"), "TRY", Transaction.TransactionTypes.INCOME, "maas");

        // INCOME oldugu icin addSpending ASLA cagrilmamali
        verify(budgetService, never()).addSpending(any(), any(), any(), any(), any());
    }
}
