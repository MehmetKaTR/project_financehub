package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.Category;
import com.mehmetkatr.financehub.entity.Transaction;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.CategoryRepository;
import com.mehmetkatr.financehub.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CategoryRepository categoryRepository;

    public List<Transaction> findByBankAccountId(Long bankAccountId) {
        return transactionRepository.findByBankAccountId(bankAccountId);
    }

    public Transaction createTransaction(Long bankAccountId, Long categoryId, BigDecimal amount, String currency, Transaction.TransactionTypes transactionTypes, String description){

        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new RuntimeException("Bank Account not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction newTransaction = Transaction.builder()
                .bankAccount(bankAccount)
                .category(category)
                .amount(amount)
                .currency(currency)
                .transactionType(transactionTypes)
                .description(description).build();

        return transactionRepository.save(newTransaction);
    }

}
