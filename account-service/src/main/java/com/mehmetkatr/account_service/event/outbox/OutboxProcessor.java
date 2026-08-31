package com.mehmetkatr.account_service.entity;

import com.mehmetkatr.account_service.mapper.BankAccountMapper;
import com.mehmetkatr.account_service.repository.BankAccountRepository;
import com.mehmetkatr.account_service.repository.OutboxRepository;
import com.mehmetkatr.account_service.repository.redis.BankAccountReadRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final BankAccountRepository bankAccountRepository;   // Oracle'dan güncel çek
    private final BankAccountReadRepository readRepository;       // Redis'e yaz
    private final BankAccountMapper bankAccountMapper;

    @Scheduled(fixedDelay = 2000)   // her 2 saniyede bir çalış
    @Transactional
    public void processOutbox() {
        List<OutboxEvent> pending = outboxRepository.findByProcessedFalse();

        for (OutboxEvent event : pending) {
            bankAccountRepository.findById(event.getAggregateId())
                    .ifPresent(account -> readRepository.save(bankAccountMapper.toReadModel(account)));
            event.setProcessed(true);
            outboxRepository.save(event);
        }
    }
}
