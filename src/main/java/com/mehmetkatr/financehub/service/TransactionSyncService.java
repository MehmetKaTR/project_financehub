package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.dto.qnb.QnbTransactionDto;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.dto.qnb.QnbStatementResponse;
import com.mehmetkatr.financehub.entity.Transaction;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionSyncService {

    private final QnbApiService qnbApiService;
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public int syncDay(Long bankAccountId, LocalDate day){

        BankAccount account = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        QnbStatementResponse resp = qnbApiService.getStatement(
                account.getIban(),
                day.atStartOfDay(),
                day.atTime(23, 59, 59));

        int imported = 0;
        for (QnbTransactionDto dto : resp.getAccountTransactionList()) {
            // ayni islemi iki kez kaydetme (mukerrer kontrolu)
            if (transactionRepository.existsByReferenceNumber(
                    dto.getProductOperationRefNo())) {
                continue;
            }
            Transaction trx = Transaction.builder()
                    .bankAccount(account)
                    .amount(new BigDecimal(dto.getTransactionAmount()))
                    .currency(dto.getCurrencyCode())
                    .transactionType("A".equals(dto.getDebitOrCreditCode())
                            ? Transaction.TransactionTypes.INCOME
                            : Transaction.TransactionTypes.EXPENSE)
                    .description(dto.getTransactionDescription())
                    .referenceNumber(dto.getProductOperationRefNo())
                    .build();

            transactionRepository.save(trx);
            imported++;
        }

        // hesap bakiyesini bankadaki guncel degere cek
        account.setBalance(new BigDecimal(resp.getAccountBalance()));

        bankAccountRepository.save(account);

        return imported;
    }

}
