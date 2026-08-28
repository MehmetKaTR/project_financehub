package com.mehmetkatr.account_service.service.query;

import com.mehmetkatr.account_service.dto.response.BankAccountResponse;
import com.mehmetkatr.account_service.entity.BankAccount;
import com.mehmetkatr.account_service.mapper.BankAccountMapper;
//import com.mehmetkatr.account_service.repository.BankAccountRepository;
import com.mehmetkatr.account_service.repository.redis.BankAccountReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountQueryService {

    //private final BankAccountRepository bankAccountRepository;
    private final BankAccountReadRepository readRepository;
    private final BankAccountMapper bankAccountMapper;

    public Optional<BankAccountResponse> findById(Long id){
        return readRepository.findById(id).map(bankAccountMapper::readModelToResponse);
    }

    public List<BankAccountResponse> findByUserId(Long id){
        return readRepository.findByUserId(id).stream().map(bankAccountMapper::readModelToResponse).toList();
    }

    public List<BankAccountResponse> findByBankName(String bankName) {
        return readRepository.findByBankName(bankName).stream().map(bankAccountMapper::readModelToResponse).toList();
    }

    public List<BankAccountResponse> findByAccountType(BankAccount.AccountType type){
        return readRepository.findByAccountType(type.toString()).stream().map(bankAccountMapper::readModelToResponse).toList();
    }
}
