package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.port.BankStatementPort;
import com.mehmetkatr.financehub.port.StatementLine;
import com.mehmetkatr.financehub.port.StatementResult;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionSyncServiceTest {

    @Mock
    private BankStatementPort bankStatementPort;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private TransactionSyncService transactionSyncService;

    private BankAccount sahteHesap() {
        return BankAccount.builder()
                .id(1L)
                .iban("TR123")
                .balance(new Money(new BigDecimal("500"), "TRY"))
                .build();
    }

    private StatementLine satir(String amount, String debitCredit, String ref) {
        return new StatementLine(
                new Money(new BigDecimal(amount), "TRY"), debitCredit, "aciklama", ref);
    }

    @Test
    void syncDay_hesapYoksa_ResourceNotFoundException() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionSyncService.syncDay(1L, LocalDate.now()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Account not found");
    }

    @Test
    void syncDay_yeniIslemler_kaydedilirVeSayiDoner() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(sahteHesap()));
        when(bankStatementPort.fetchStatement(anyString(), any(), any()))
                .thenReturn(new StatementResult(
                        List.of(satir("100", "A", "REF1"), satir("50", "B", "REF2")),
                        new Money(new BigDecimal("450"), "TRY")));
        // iki islem de yeni (mukerrer degil)
        when(transactionRepository.existsByReferenceNumber(anyString())).thenReturn(false);

        int imported = transactionSyncService.syncDay(1L, LocalDate.now());

        assertThat(imported).isEqualTo(2);
        verify(transactionRepository, times(2)).save(any());
    }

    @Test
    void syncDay_mukerrerIslem_atlanir() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(sahteHesap()));
        when(bankStatementPort.fetchStatement(anyString(), any(), any()))
                .thenReturn(new StatementResult(
                        List.of(satir("100", "A", "REF1")),
                        new Money(new BigDecimal("450"), "TRY")));
        // islem zaten var -> atlanmali
        when(transactionRepository.existsByReferenceNumber("REF1")).thenReturn(true);

        int imported = transactionSyncService.syncDay(1L, LocalDate.now());

        assertThat(imported).isEqualTo(0);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void syncDay_hesapBakiyesi_closingBalanceIleGuncellenir() {
        BankAccount account = sahteHesap();
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(bankStatementPort.fetchStatement(anyString(), any(), any()))
                .thenReturn(new StatementResult(
                        List.of(),
                        new Money(new BigDecimal("999"), "TRY")));

        transactionSyncService.syncDay(1L, LocalDate.now());

        // hesap bakiyesi bankadan gelen closingBalance ile guncellendi mi?
        assertThat(account.getBalance().getAmount()).isEqualByComparingTo(new BigDecimal("999"));
        verify(bankAccountRepository).save(account);
    }
}
