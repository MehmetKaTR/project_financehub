package com.mehmetkatr.financehub.repository;

import com.mehmetkatr.financehub.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByBankAccountId(Long id);

    List<Transaction> findByCategoryId(Long id);

    List<Transaction> findByTransactionType(Transaction.TransactionTypes transactionType);

    boolean existsByReferenceNumber(String referenceNumber);

}
