package com.mehmetkatr.account_service.repository;

import com.mehmetkatr.account_service.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByUserId(Long userId);
    List<BankAccount> findByBankName(String bankName);
    List<BankAccount> findByAccountType(BankAccount.AccountType accountType);
}
