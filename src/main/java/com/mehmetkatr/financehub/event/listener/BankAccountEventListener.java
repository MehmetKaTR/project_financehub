package com.mehmetkatr.financehub.event.listener;

import com.mehmetkatr.financehub.event.BankAccountChangedEvent;
import com.mehmetkatr.financehub.mapper.BankAccountMapper;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.redis.BankAccountReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BankAccountEventListener {

    private final BankAccountRepository bankAccountRepository;      // Oracle'dan güncel veri çekmek için
    private final BankAccountReadRepository readRepository;         // Redis'e yazmak için
    private final BankAccountMapper bankAccountMapper;              // entity → readModel

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBankAccountChanged(BankAccountChangedEvent event){
        bankAccountRepository.findById(event.accountId())
                .ifPresent(account ->
                        readRepository.save(bankAccountMapper.toReadModel(account)));
    }

}
