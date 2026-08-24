package com.mehmetkatr.financehub.service.command;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.mapper.BankAccountMapper;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountCommandServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @InjectMocks
    private BankAccountCommandService commandService;

    private BankAccount aktifHesap(String bakiye) {
        return BankAccount.builder()
                .user(User.builder().id(5L).build())
                .balance(new Money(new BigDecimal(bakiye), "TRY"))
                .isActive(true)
                .build();
    }

    @Test
    void withdraw_yetersizBakiye_exceptionFirlatir() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(aktifHesap("100")));

        assertThatThrownBy(() -> commandService.withdraw(1L, new BigDecimal("500")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Insufficient balance");
    }

    @Test
    void withdraw_pasifHesap_exceptionFirlatir() {
        BankAccount pasif = BankAccount.builder()
                .balance(new Money(new BigDecimal("200"), "TRY"))
                .isActive(false)
                .build();
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(pasif));

        assertThatThrownBy(() -> commandService.withdraw(1L, new BigDecimal("50")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Bank account is not active");
    }

    @Test
    void withdraw_basarili_bakiyeyiDuser() {
        BankAccount account = aktifHesap("500");
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(account)).thenReturn(account);

        BankAccount sonuc = commandService.withdraw(1L, new BigDecimal("200"));

        assertThat(sonuc.getBalance().getAmount()).isEqualByComparingTo(new BigDecimal("300"));
    }

    @Test
    void deposit_basarili_bakiyeyiArtirir() {
        BankAccount account = aktifHesap("500");
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(bankAccountMapper.toResponse(any())).thenReturn(new BankAccountResponse());

        commandService.deposit(1L, new BigDecimal("200"));

        assertThat(account.getBalance().getAmount()).isEqualByComparingTo(new BigDecimal("700"));
    }

    @Test
    void createBankAccount_kullaniciYoksa_ResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commandService.createBankAccount(
                1L, "QNB", "123", "TR123",
                new Money(new BigDecimal("1000"), "TRY"), BankAccount.AccountType.CHECKING))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }
}
