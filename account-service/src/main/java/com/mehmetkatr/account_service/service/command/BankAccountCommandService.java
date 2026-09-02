package com.mehmetkatr.account_service.service.command;

import com.mehmetkatr.account_service.domain.Money;
import com.mehmetkatr.account_service.dto.response.BankAccountResponse;
import com.mehmetkatr.account_service.entity.BankAccount;
import com.mehmetkatr.account_service.entity.OutboxEvent;
import com.mehmetkatr.account_service.exception.ResourceNotFoundException;
import com.mehmetkatr.account_service.mapper.BankAccountMapper;
import com.mehmetkatr.account_service.repository.BankAccountRepository;
import com.mehmetkatr.account_service.repository.OutboxRepository;
//import com.mehmetkatr.account_service.repository.redis.BankAccountReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.ObjectProvider;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankAccountCommandService {

    private final BankAccountRepository bankAccountRepository;
    //private final BankAccountReadRepository readRepository;
    private final BankAccountMapper bankAccountMapper;
    //private final ApplicationEventPublisher eventPublisher;
    private final OutboxRepository outboxRepository;

    private final ObjectProvider<BankAccountCommandService> selfProvider;

    @Transactional
    public BankAccountResponse createBankAccount(Long userId, String bankName, String bankAccountNumber, String iban, Money balance, BankAccount.AccountType type){

        // Mikroservis: User nesnesi YOK, sadece userId referansı tutulur.
        // (Gerekirse user-service'e Feign ile sorulur; burada dogrulama yapilmaz.)
        BankAccount newBankAccount = BankAccount.builder()
                .userId(userId)
                .bankName(bankName)
                .bankAccountNumber(bankAccountNumber)
                .iban(iban)
                .balance(balance)
                .accountType(type)
                .isActive(true)
                .build();

        bankAccountRepository.save(newBankAccount);
        //readRepository.save(bankAccountMapper.toReadModel(newBankAccount));
        //eventPublisher.publishEvent(new BankAccountChangedEvent(newBankAccount.getId()));
        writeToOutbox(newBankAccount.getId());

        return bankAccountMapper.toResponse(newBankAccount);
    }

    @Transactional
    public BankAccountResponse activateAccount(Long id){
        BankAccount currentBankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bank Account not found"));

        if(!currentBankAccount.isActive())
            currentBankAccount.setActive(true);

        bankAccountRepository.save(currentBankAccount);
        writeToOutbox(currentBankAccount.getId());

        return bankAccountMapper.toResponse(currentBankAccount);
    }

    @Transactional
    public BankAccountResponse deactivateAccount(Long id){
        BankAccount currentBankAccount = bankAccountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bank Account not found"));

        if(currentBankAccount.isActive())
            currentBankAccount.setActive(false);

        bankAccountRepository.save(currentBankAccount);
        writeToOutbox(currentBankAccount.getId());

        return bankAccountMapper.toResponse(currentBankAccount);
    }

    @Transactional
    public BankAccountResponse deposit(Long accountId, BigDecimal amount){
        BankAccount account = bankAccountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Bank account not found"));

        Money balance = new Money(amount, account.getBalance().getCurrency());
        account.deposit(balance);

        bankAccountRepository.save(account);
        writeToOutbox(account.getId());

        return bankAccountMapper.toResponse(account);
    }

    @Transactional
    public BankAccount withdraw(Long accountId, BigDecimal amount){
        BankAccount account = bankAccountRepository.findById(accountId).orElseThrow(()-> new ResourceNotFoundException("Bank account not found"));

        Money balance = new Money(amount, account.getBalance().getCurrency());
        account.withdraw(balance);

        bankAccountRepository.save(account);
        writeToOutbox(account.getId());

        return account;
    }

    @Transactional
    public BankAccountResponse withdrawForApi(Long accountId, BigDecimal amount) {
        return bankAccountMapper.toResponse(selfProvider.getObject().withdraw(accountId, amount));
    }

    private void writeToOutbox(Long accountId) {
        outboxRepository.save(OutboxEvent.builder()
                .aggregateType("BankAccount")
                .aggregateId(accountId)
                .eventType("CHANGED")
                .processed(false)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
