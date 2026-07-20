package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    public Optional<BankAccountResponse> findById(Long id){
        return bankAccountRepository.findById(id).map(this::toResponse);
    }

    public List<BankAccountResponse> findByUserId(Long id){
        return bankAccountRepository.findByUserId(id).stream().map(this::toResponse).toList();
    }

    public List<BankAccountResponse> findByBankName(String bankName) {

        return bankAccountRepository.findByBankName(bankName).stream().map(this::toResponse).toList();
    }

    public List<BankAccountResponse> findByAccountType(BankAccount.AccountType type){
        return bankAccountRepository.findByAccountType(type).stream().map(this::toResponse).toList();
    }

    public BankAccountResponse createBankAccount(Long userId, String bankName, String bankAccountNumber, String iban, String currency, BigDecimal balance, BankAccount.AccountType type){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BankAccount newBankAccount = BankAccount.builder()
                .user(user)
                .bankName(bankName)
                .bankAccountNumber(bankAccountNumber)
                .iban(iban)
                .currency(currency)
                .balance(balance)
                .accountType(type)
                .isActive(true)
                .build();

        bankAccountRepository.save(newBankAccount);

        return toResponse(newBankAccount);
    }

    public BankAccountResponse activateAccount(Long id){
        BankAccount currentBankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bank Account not found"));

        if(!currentBankAccount.isActive())
            currentBankAccount.setActive(true);

        bankAccountRepository.save(currentBankAccount);

        return toResponse(currentBankAccount);
    }

    public BankAccountResponse deactivateAccount(Long id){
        BankAccount currentBankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bank Account not found"));

        if(currentBankAccount.isActive())
            currentBankAccount.setActive(false);

        bankAccountRepository.save(currentBankAccount);

        return toResponse(currentBankAccount);
    }

    @Transactional
    public BankAccountResponse deposit(Long accountId, BigDecimal amount){
        BankAccount currentBankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        if (!currentBankAccount.isActive()) {
            throw new RuntimeException("Bank account is not active");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }

        BigDecimal newBalance = currentBankAccount.getBalance().add(amount);
        currentBankAccount.setBalance(newBalance);
        bankAccountRepository.save(currentBankAccount);

        return toResponse(currentBankAccount);
    }

    @Transactional
    public BankAccount withdraw(Long accountId,BigDecimal amount){
        BankAccount currentBankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        if (!currentBankAccount.isActive()) {
            throw new RuntimeException("Bank account is not active");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdraw amount must be positive");
        }

        if (currentBankAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        BigDecimal newBalance = currentBankAccount.getBalance().subtract(amount);
        currentBankAccount.setBalance(newBalance);

        return bankAccountRepository.save(currentBankAccount);

    }

    @Transactional
    public BankAccountResponse withdrawForApi(Long accountId, BigDecimal amount) {
        return toResponse(withdraw(accountId, amount));
    }

    // convert BankAccount to BankAccountResponse
    private BankAccountResponse toResponse(BankAccount bankAccount) {
        BankAccountResponse response = new BankAccountResponse();
        response.setId(bankAccount.getId());
        response.setUserId(bankAccount.getUser().getId());
        response.setBankName(bankAccount.getBankName());
        response.setBankAccountNumber(bankAccount.getBankAccountNumber());
        response.setIban(bankAccount.getIban());
        response.setCurrency(bankAccount.getCurrency());
        response.setBalance(bankAccount.getBalance());
        response.setAccountType(bankAccount.getAccountType());
        response.setActive(bankAccount.isActive());

        return response;
    }

}
