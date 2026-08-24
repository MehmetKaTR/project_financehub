package com.mehmetkatr.financehub.mapper;

import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    // convert BankAccount to BankAccountResponse
    public BankAccountResponse toResponse(BankAccount bankAccount) {
        BankAccountResponse response = new BankAccountResponse();
        response.setId(bankAccount.getId());
        response.setUserId(bankAccount.getUser().getId());
        response.setBankName(bankAccount.getBankName());
        response.setBankAccountNumber(bankAccount.getBankAccountNumber());
        response.setIban(bankAccount.getIban());
        response.setBalance(bankAccount.getBalance().getAmount());
        response.setCurrency(bankAccount.getBalance().getCurrency());
        response.setAccountType(bankAccount.getAccountType());
        response.setActive(bankAccount.isActive());

        return response;
    }
}
