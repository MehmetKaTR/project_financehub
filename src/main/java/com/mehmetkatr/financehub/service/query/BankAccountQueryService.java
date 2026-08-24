package com.mehmetkatr.financehub.service.query;

import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.mapper.BankAccountMapper;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountQueryService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    public Optional<BankAccountResponse> findById(Long id){
        return bankAccountRepository.findById(id).map(bankAccountMapper::toResponse);
    }

    public List<BankAccountResponse> findByUserId(Long id){
        return bankAccountRepository.findByUserId(id).stream().map(bankAccountMapper::toResponse).toList();
    }

    public List<BankAccountResponse> findByBankName(String bankName) {

        return bankAccountRepository.findByBankName(bankName).stream().map(bankAccountMapper::toResponse).toList();
    }

    public List<BankAccountResponse> findByAccountType(BankAccount.AccountType type){
        return bankAccountRepository.findByAccountType(type).stream().map(bankAccountMapper::toResponse).toList();
    }

}
