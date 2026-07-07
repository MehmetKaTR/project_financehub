package com.mehmetkatr.financehub.repository;

import com.mehmetkatr.financehub.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findByUserId(Long userId);

    List<BankAccount> findByBankName(String bankName);

    List<BankAccount> findByAccountType(BankAccount.AccountType accountType);
}
