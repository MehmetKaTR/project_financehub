package com.mehmetkatr.account_service.mapper;

import com.mehmetkatr.account_service.dto.response.BankAccountResponse;
import com.mehmetkatr.account_service.entity.BankAccount;
import com.mehmetkatr.account_service.service.query.model.BankAccountReadModel;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    // convert BankAccount to BankAccountResponse
    public BankAccountResponse toResponse(BankAccount bankAccount) {
        BankAccountResponse response = new BankAccountResponse();
        response.setId(bankAccount.getId());
        response.setUserId(bankAccount.getUserId());          // userId — artık User nesnesi yok
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
        readModel.setUserId(bankAccount.getUserId());          // userId
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

        return response;
    }
}
