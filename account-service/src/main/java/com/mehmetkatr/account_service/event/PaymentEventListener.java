package com.mehmetkatr.account_service.event;

import com.mehmetkatr.account_service.domain.Money;
import com.mehmetkatr.account_service.entity.BankAccount;
import com.mehmetkatr.account_service.entity.Transaction;
import com.mehmetkatr.account_service.repository.BankAccountRepository;
import com.mehmetkatr.account_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;

    @KafkaListener(topics = "payment-events", groupId = "account-service-group")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        BankAccount account = bankAccountRepository.findById(event.bankAccountId()).orElse(null);
        if (account == null) return;   // hesap yoksa atla

        Transaction tx = Transaction.builder()
                .bankAccount(account)
                .balance(new Money(event.amount(), event.currency()))
                .transactionType(Transaction.TransactionTypes.EXPENSE)
                .description("Transfer: " + event.toName())
                .build();

        transactionRepository.save(tx);
    }
}