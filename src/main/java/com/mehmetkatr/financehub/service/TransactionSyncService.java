package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.Transaction;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.port.BankStatementPort;
import com.mehmetkatr.financehub.port.StatementLine;
import com.mehmetkatr.financehub.port.StatementResult;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionSyncService {

    private final BankStatementPort bankStatementPort;
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    @Transactional
    public int syncDay(Long bankAccountId, LocalDate day){

        BankAccount account = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        StatementResult result = bankStatementPort.fetchStatement(account.getIban(),day.atStartOfDay(),
                day.atTime(23, 59, 59));

        int imported = 0;
        for (StatementLine line: result.lines()) {
            // ayni islemi iki kez kaydetme (mukerrer kontrolu)
            if (transactionRepository.existsByReferenceNumber(
                    line.referenceNumber())) {
                continue;
            }
            Transaction trx = Transaction.builder()
                    .bankAccount(account)
                    .balance(line.amount())
                    .transactionType("A".equals(line.debitOrCreditCode())
                            ? Transaction.TransactionTypes.INCOME
                            : Transaction.TransactionTypes.EXPENSE)
                    .description(line.transactionDescription())
                    .referenceNumber(line.referenceNumber())
                    .build();

            transactionRepository.save(trx);
            imported++;
        }

        // bakiye artık result'tan geliyor, resp'ten değil
        account.setBalance(result.closingBalance());
        bankAccountRepository.save(account);

        return imported;
    }

}
