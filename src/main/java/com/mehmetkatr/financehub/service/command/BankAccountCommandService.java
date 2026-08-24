package com.mehmetkatr.financehub.service.command;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.mapper.BankAccountMapper;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankAccountCommandService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final BankAccountMapper bankAccountMapper;

    public BankAccountResponse createBankAccount(Long userId, String bankName, String bankAccountNumber, String iban, Money balance, BankAccount.AccountType type){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BankAccount newBankAccount = BankAccount.builder()
                .user(user)
                .bankName(bankName)
                .bankAccountNumber(bankAccountNumber)
                .iban(iban)
                .balance(balance)
                .accountType(type)
                .isActive(true)
                .build();

        bankAccountRepository.save(newBankAccount);

        return bankAccountMapper.toResponse(newBankAccount);
    }

    public BankAccountResponse activateAccount(Long id){
        BankAccount currentBankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bank Account not found"));

        if(!currentBankAccount.isActive())
            currentBankAccount.setActive(true);

        bankAccountRepository.save(currentBankAccount);

        return bankAccountMapper.toResponse(currentBankAccount);
    }

    public BankAccountResponse deactivateAccount(Long id){
        BankAccount currentBankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bank Account not found"));

        if(currentBankAccount.isActive())
            currentBankAccount.setActive(false);

        bankAccountRepository.save(currentBankAccount);

        return bankAccountMapper.toResponse(currentBankAccount);
    }

    @Transactional
    public BankAccountResponse deposit(Long accountId, BigDecimal amount){
        BankAccount account = bankAccountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        Money balance = new Money(amount, account.getBalance().getCurrency());
        account.deposit(balance);

        bankAccountRepository.save(account);
        return bankAccountMapper.toResponse(account);
    }

    @Transactional
    public BankAccount withdraw(Long accountId, BigDecimal amount){
        BankAccount account = bankAccountRepository.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Bank account not found"));

        Money balance = new Money(amount, account.getBalance().getCurrency());
        account.withdraw(balance);

        bankAccountRepository.save(account);
        return account;
    }

    @Transactional
    public BankAccountResponse withdrawForApi(Long accountId, BigDecimal amount) {
        return bankAccountMapper.toResponse(withdraw(accountId, amount));
    }
}
