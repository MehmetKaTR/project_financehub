package com.mehmetkatr.financehub.mapper;

import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.service.query.model.BankAccountReadModel;
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

    public BankAccountReadModel toReadModel(BankAccount bankAccount){
        BankAccountReadModel readModel = new BankAccountReadModel();
        readModel.setId(bankAccount.getId());
        readModel.setUserId(bankAccount.getUser().getId());
        readModel.setBankName(bankAccount.getBankName());
        readModel.setIban(bankAccount.getIban());
        readModel.setBalance(bankAccount.getBalance().getAmount());
        readModel.setCurrency(bankAccount.getBalance().getCurrency());
        readModel.setBankAccountNumber(bankAccount.getBankAccountNumber());
        readModel.setAccountType(bankAccount.getAccountType().toString());
        readModel.setActive(bankAccount.isActive());

        return readModel;
    }

    public BankAccountResponse readModelToResponse(BankAccountReadModel readModel){
        BankAccountResponse response = new BankAccountResponse();
        response.setId(readModel.getId());
        response.setUserId(readModel.getUserId());
        response.setBankName(readModel.getBankName());
        response.setIban(readModel.getIban());
        response.setCurrency(readModel.getCurrency());
        response.setBankAccountNumber(readModel.getBankAccountNumber());
        response.setBalance(readModel.getBalance());
        response.setAccountType(BankAccount.AccountType.valueOf(readModel.getAccountType()));
        response.setActive(readModel.isActive());

        return  response;
    }

}
