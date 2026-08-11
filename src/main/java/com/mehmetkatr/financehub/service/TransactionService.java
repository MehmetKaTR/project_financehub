package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.response.TransactionResponse;
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

    public List<TransactionResponse> findByBankAccountId(Long bankAccountId) {
        return transactionRepository.findByBankAccountId(bankAccountId).stream().map(this::toResponse).toList();
    }

    @Transactional
    public TransactionResponse createTransaction(Long bankAccountId, Long categoryId, BigDecimal amount, String currency, Transaction.TransactionTypes transactionTypes, String description){

        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found"));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Money money = new Money(amount, currency);

        Transaction newTransaction = bankAccount.addTransaction(money, category, transactionTypes, description);

        transactionRepository.save(newTransaction);

        if (newTransaction.getTransactionType() == Transaction.TransactionTypes.EXPENSE)
            budgetService.addSpending(
                    bankAccount.getUser().getId(),
                    categoryId,
                    LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue(),
                    money.getAmount()
            );

        return toResponse(newTransaction);
    }

    // convert Transaction to TransactionResponse
    private TransactionResponse toResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setBankAccountId(transaction.getBankAccount().getId());
        if (transaction.getCategory() != null) {
            response.setCategoryId(transaction.getCategory().getId());
        }
        response.setAmount(transaction.getAmount());
        response.setCurrency(transaction.getCurrency());
        response.setType(transaction.getTransactionType());
        response.setDescription(transaction.getDescription());
        response.setReferenceNumber(transaction.getReferenceNumber());

        return response;
    }

}
