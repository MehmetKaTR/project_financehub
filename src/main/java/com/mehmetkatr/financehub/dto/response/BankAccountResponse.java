package com.mehmetkatr.financehub.dto.response;

import com.mehmetkatr.financehub.entity.BankAccount;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankAccountResponse {
    private Long id;
    private Long userId;
    private String bankName;
    private String bankAccountNumber;
    private String iban;
    private String currency;
    private BigDecimal balance;
    private BankAccount.AccountType accountType;
    private boolean isActive;
}
