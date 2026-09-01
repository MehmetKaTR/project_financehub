package com.mehmetkatr.account_service.entity;

import com.mehmetkatr.account_service.domain.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BankAccountTest {

    private BankAccount aktifHesap(String bakiye) {
        return BankAccount.builder()
                .userId(1L)
                .bankName("QNB")
                .balance(new Money(new BigDecimal(bakiye), "TRY"))
                .accountType(BankAccount.AccountType.CHECKING)
                .isActive(true)
                .build();
    }

    @Test
    void withdraw_bakiyeyi_dusurur() {
        BankAccount hesap = aktifHesap("100");

        hesap.withdraw(new Money(new BigDecimal("30"), "TRY"));

        assertThat(hesap.getBalance().getAmount()).isEqualByComparingTo("70");
    }

    @Test
    void withdraw_yetersiz_bakiyede_hata_verir() {
        BankAccount hesap = aktifHesap("50");

        assertThatThrownBy(() -> hesap.withdraw(new Money(new BigDecimal("100"), "TRY")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    void pasif_hesaptan_withdraw_yapilamaz() {
        BankAccount hesap = aktifHesap("100");
        hesap.setActive(false);

        assertThatThrownBy(() -> hesap.withdraw(new Money(new BigDecimal("10"), "TRY")))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not active");
    }

    @Test
    void deposit_bakiyeyi_artirir() {
        BankAccount hesap = aktifHesap("100");

        hesap.deposit(new Money(new BigDecimal("50"), "TRY"));

        assertThat(hesap.getBalance().getAmount()).isEqualByComparingTo("150");
    }
}
