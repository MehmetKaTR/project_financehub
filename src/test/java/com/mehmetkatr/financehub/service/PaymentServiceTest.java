package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.dto.qnb.QnbMoneyTransferRequest;
import com.mehmetkatr.financehub.dto.qnb.QnbMoneyTransferResponse;
import com.mehmetkatr.financehub.dto.response.PaymentResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.Payment;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.PaymentRepository;
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
    private BankAccountService bankAccountService;

    @Mock
    private QnbApiService qnbApiService;

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

    // QNB'nin belirli bir resultCode ile donecegi cevabi hazirlar
    private QnbMoneyTransferResponse qnbCevap(String resultCode) {
        QnbMoneyTransferResponse resp = new QnbMoneyTransferResponse();
        resp.setResultCode(resultCode);
        return resp;
    }

    @Test
    void transferMoney_resultCode000_statusCompleted() {
        when(bankAccountService.withdraw(1L, new BigDecimal("100"))).thenReturn(sahteHesap());
        when(qnbApiService.transferMoney(any(QnbMoneyTransferRequest.class))).thenReturn(qnbCevap("000"));

        PaymentResponse sonuc = paymentService.transferMoney(
                1L, "TR123", "Alici", new BigDecimal("100"), "TRY", null, null);

        assertThat(sonuc.getStatus()).isEqualTo(Payment.PaymentStatus.COMPLETED);
    }

    @Test
    void transferMoney_resultCode998_statusCompleted() {
        when(bankAccountService.withdraw(1L, new BigDecimal("100"))).thenReturn(sahteHesap());
        when(qnbApiService.transferMoney(any(QnbMoneyTransferRequest.class))).thenReturn(qnbCevap("998"));

        PaymentResponse sonuc = paymentService.transferMoney(
                1L, "TR123", "Alici", new BigDecimal("100"), "TRY", null, null);

        assertThat(sonuc.getStatus()).isEqualTo(Payment.PaymentStatus.COMPLETED);
    }

    @Test
    void transferMoney_baskaResultCode_statusFailed() {
        when(bankAccountService.withdraw(1L, new BigDecimal("100"))).thenReturn(sahteHesap());
        when(qnbApiService.transferMoney(any(QnbMoneyTransferRequest.class))).thenReturn(qnbCevap("007"));

        PaymentResponse sonuc = paymentService.transferMoney(
                1L, "TR123", "Alici", new BigDecimal("100"), "TRY", null, null);

        assertThat(sonuc.getStatus()).isEqualTo(Payment.PaymentStatus.FAILED);
    }

    @Test
    void transferMoney_onceWithdrawCagrilir() {
        when(bankAccountService.withdraw(1L, new BigDecimal("100"))).thenReturn(sahteHesap());
        when(qnbApiService.transferMoney(any(QnbMoneyTransferRequest.class))).thenReturn(qnbCevap("000"));

        paymentService.transferMoney(
                1L, "TR123", "Alici", new BigDecimal("100"), "TRY", null, null);

        // bakiye dusme adimi (withdraw) gercekten cagrildi mi?
        verify(bankAccountService).withdraw(1L, new BigDecimal("100"));
    }
}
