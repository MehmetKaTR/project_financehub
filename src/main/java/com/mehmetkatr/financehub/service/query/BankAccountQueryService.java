package com.mehmetkatr.financehub.service.query;

import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.mapper.BankAccountMapper;
//import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.redis.BankAccountReadRepository;
import com.mehmetkatr.financehub.service.query.model.BankAccountReadModel;
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
