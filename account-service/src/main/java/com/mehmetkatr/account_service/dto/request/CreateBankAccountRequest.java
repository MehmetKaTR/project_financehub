package com.mehmetkatr.account_service.dto.request;

import com.mehmetkatr.account_service.entity.BankAccount;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBankAccountRequest {
    private Long userId;
    private String bankName;
    private String bankAccountNumber;
    private String iban;
    private String currency;
    private BigDecimal balance;
    private BankAccount.AccountType accountType;
}
