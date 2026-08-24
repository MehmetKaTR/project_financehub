package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.response.PaymentResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.Payment;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.port.BankTransferPort;
import com.mehmetkatr.financehub.port.TransferCommand;
import com.mehmetkatr.financehub.port.TransferResult;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.PaymentRepository;
import com.mehmetkatr.financehub.service.command.BankAccountCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountCommandService bankAccountCommandService;

    @Mock
    private BankTransferPort bankTransferPort;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    // ortak sahte hesap: withdraw'in donecegi entity
    private BankAccount sahteHesap() {
        return BankAccount.builder()
                .id(1L)
                .user(User.builder().id(5L).build())
                .bankAccountNumber("21385331")
                .build();
    }

    // port'un basarili / basarisiz donecegi sonucu hazirlar
    private TransferResult transferSonucu(boolean success) {
        return new TransferResult(success, "SLIP-123");
    }

    // gercek DB save() Payment'a id atar; mock'ta bunu taklit ediyoruz
    private void saveIdAtar() {
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            p.setId(99L);
            return p;
        });
    }

    @Test
    void transferMoney_transferBasarili_statusCompleted() {
        saveIdAtar();
        when(bankAccountCommandService.withdraw(1L, new BigDecimal("100"))).thenReturn(sahteHesap());
        when(bankTransferPort.transfer(any(TransferCommand.class))).thenReturn(transferSonucu(true));

        PaymentResponse sonuc = paymentService.transferMoney(
                1L, "TR123", "Alici", new Money(new BigDecimal("100"), "TRY"), null, null);

        assertThat(sonuc.getStatus()).isEqualTo(Payment.PaymentStatus.COMPLETED);
    }

    @Test
    void transferMoney_transferBasarisiz_statusFailed() {
        saveIdAtar();
        when(bankAccountCommandService.withdraw(1L, new BigDecimal("100"))).thenReturn(sahteHesap());
        when(bankTransferPort.transfer(any(TransferCommand.class))).thenReturn(transferSonucu(false));

        PaymentResponse sonuc = paymentService.transferMoney(
                1L, "TR123", "Alici", new Money(new BigDecimal("100"), "TRY"), null, null);

        assertThat(sonuc.getStatus()).isEqualTo(Payment.PaymentStatus.FAILED);
    }

    @Test
    void transferMoney_onceWithdrawCagrilir() {
        saveIdAtar();
        when(bankAccountCommandService.withdraw(1L, new BigDecimal("100"))).thenReturn(sahteHesap());
        when(bankTransferPort.transfer(any(TransferCommand.class))).thenReturn(transferSonucu(true));

        paymentService.transferMoney(
                1L, "TR123", "Alici", new Money(new BigDecimal("100"), "TRY"), null, null);

        // bakiye dusme adimi (withdraw) gercekten cagrildi mi?
        verify(bankAccountCommandService).withdraw(1L, new BigDecimal("100"));
    }

    @Test
    void transferMoney_portCagrilir() {
        saveIdAtar();
        when(bankAccountCommandService.withdraw(1L, new BigDecimal("100"))).thenReturn(sahteHesap());
        when(bankTransferPort.transfer(any(TransferCommand.class))).thenReturn(transferSonucu(true));

        paymentService.transferMoney(
                1L, "TR123", "Alici", new Money(new BigDecimal("100"), "TRY"), null, null);

        // transfer gercekten port uzerinden yapildi mi? (QNB'yi bilmiyoruz, sadece port'u)
        verify(bankTransferPort).transfer(any(TransferCommand.class));
    }
}
