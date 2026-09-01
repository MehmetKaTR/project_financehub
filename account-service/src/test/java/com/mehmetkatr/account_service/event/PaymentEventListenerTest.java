package com.mehmetkatr.account_service.event;

import com.mehmetkatr.account_service.domain.Money;
import com.mehmetkatr.account_service.entity.BankAccount;
import com.mehmetkatr.account_service.entity.Transaction;
import com.mehmetkatr.account_service.repository.BankAccountRepository;
import com.mehmetkatr.account_service.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEventListenerTest {

    @Mock BankAccountRepository bankAccountRepository;
    @Mock TransactionRepository transactionRepository;
    @InjectMocks PaymentEventListener listener;

    private PaymentCompletedEvent event() {
        return new PaymentCompletedEvent(99L, 1L, new BigDecimal("100"), "TRY", "Ahmet");
    }

    @Test
    void hesap_bulununca_transaction_kaydeder() {
        BankAccount hesap = BankAccount.builder().id(1L).userId(5L)
                .balance(new Money(new BigDecimal("900"), "TRY"))
                .accountType(BankAccount.AccountType.CHECKING).isActive(true).build();
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(hesap));

        listener.onPaymentCompleted(event());

        // olay islenince bir Transaction kaydedilmeli
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void hesap_yoksa_transaction_kaydedilmez() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        listener.onPaymentCompleted(event());

        // hesap yoksa hicbir sey kaydedilmemeli
        verify(transactionRepository, never()).save(any());
    }
}
