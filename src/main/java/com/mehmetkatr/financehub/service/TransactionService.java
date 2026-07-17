package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.Category;
import com.mehmetkatr.financehub.entity.Transaction;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.CategoryRepository;
import com.mehmetkatr.financehub.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;
    private final BudgetService budgetService;

    public List<Transaction> findByBankAccountId(Long bankAccountId) {
        return transactionRepository.findByBankAccountId(bankAccountId);
    }

    @Transactional
    public Transaction createTransaction(Long bankAccountId, Long categoryId, BigDecimal amount, String currency, Transaction.TransactionTypes transactionTypes, String description){

        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Transaction newTransaction = Transaction.builder()
                .bankAccount(bankAccount)
                .category(category)
                .amount(amount)
                .currency(currency)
                .transactionType(transactionTypes)
                .description(description).build();

        transactionRepository.save(newTransaction);

        if (newTransaction.getTransactionType() == Transaction.TransactionTypes.EXPENSE)
            budgetService.addSpending(
                    bankAccount.getUser().getId(),
                    categoryId,
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue(),
                    amount
            );

        return newTransaction;
    }

}
