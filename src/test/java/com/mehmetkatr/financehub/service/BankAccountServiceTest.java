package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.User;
import com.mehmetkatr.financehub.exception.ResourceNotFoundException;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    // TODO: withdraw - yetersiz bakiyede exception firlatir
    @Test
    void withdraw_yetersizBakiye_exceptionFirlatir(){

        Money balance = new Money(new BigDecimal("100"), "TRY");

        // 1. ARRANGE (Hazırla) — sahte senaryoyu kur
        BankAccount account = BankAccount.builder()
                .balance(balance)
                .isActive(true)
                .build();

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        // 2 + 3. ACT + ASSERT (Çalıştır + Doğrula)
        assertThatThrownBy(() -> bankAccountService.withdraw(1L, new BigDecimal("500")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Insufficient balance");
    }

    // TODO: withdraw - pasif hesapta exception firlatir
    @Test
    void withdraw_pasifHesap_exceptionFirlatir(){

        Money balance = new Money(new BigDecimal("200"), "TRY");

        BankAccount account = BankAccount.builder()
                .balance(balance)
                .isActive(false)
                .build();

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> bankAccountService.withdraw(1L, new BigDecimal("500")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Bank account is not active");

    }

    // TODO: withdraw - negatif/sifir tutarda exception firlatir
    @Test
    void withdraw_negatifTutar_ExceptionFirlatir(){

        Money balance = new Money(new BigDecimal("200"), "TRY");

        BankAccount account = BankAccount.builder()
                .balance(balance)
                .isActive(true)
                .build();

        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        // negatif deger artik Money constructor'inda yakalaniyor (value object korumasi)
        assertThatThrownBy(() -> bankAccountService.withdraw(1L, new BigDecimal("-100")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Para negatif olamaz");

    }

    // TODO: withdraw - basarili durumda bakiyeyi dogru duser
    @Test
    void withdraw_BakiyeHarca_BasariliDus(){

        Money balance = new Money(new BigDecimal("500"), "TRY");

        BankAccount account = BankAccount.builder()
                .balance(balance)
                .isActive(true)
                .build();

        // Mocks
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(account)).thenReturn(account);

        BankAccount sonuc = bankAccountService.withdraw(1L, new BigDecimal("200"));

        assertThat(sonuc.getBalance().getAmount()).isEqualByComparingTo(new BigDecimal("300"));
    }

    // TODO: deposit - basarili durumda bakiyeyi dogru artirir
    @Test
    void deposit_BakiyeEkle_DogruArttir(){

        User user = User.builder().build();

        Money balance = new Money(new BigDecimal("500"), "TRY");

        BankAccount account = BankAccount.builder()
                .user(user)
                .balance(balance)
                .isActive(true)
                .build();

        // Mocks
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(account)).thenReturn(account);

        BankAccountResponse sonuc = bankAccountService.deposit(1L, new BigDecimal("500"));

        assertThat(sonuc.getBalance()).isEqualByComparingTo(new BigDecimal("1000"));
    }

    // TODO: deposit - negatif tutarda exception firlatir
    @Test
    void deposit_NegatifTutar_ExceptionFirlat(){

        User user = User.builder().build();

        Money balance = new Money(new BigDecimal("500"), "TRY");

        BankAccount account = BankAccount.builder()
                .user(user)
                .balance(balance)
                .isActive(true)
                .build();

        // Mocks
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        // negatif deger artik Money constructor'inda yakalaniyor (value object korumasi)
        assertThatThrownBy(() -> bankAccountService.deposit(1L, new BigDecimal("-100")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Para negatif olamaz");
    }

    // TODO: createBankAccount - kullanici yoksa ResourceNotFoundException
    @Test
    void createBankAccount_KullaniciYoksa_ResourceNotFoundExceptipn(){

        Money balance = new Money( BigDecimal.ZERO, "TRY");
        //Mocks
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bankAccountService.createBankAccount(
                1L, "QNB", "123", "TR123", balance, BankAccount.AccountType.CHECKING))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

}
