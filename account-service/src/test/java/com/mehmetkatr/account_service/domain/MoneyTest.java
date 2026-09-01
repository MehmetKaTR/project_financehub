package com.mehmetkatr.account_service.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void ayni_para_biriminde_toplama_yapar() {
        Money a = new Money(new BigDecimal("100"), "TRY");
        Money b = new Money(new BigDecimal("50"), "TRY");

        Money sonuc = a.add(b);

        assertThat(sonuc.getAmount()).isEqualByComparingTo("150");
        assertThat(sonuc.getCurrency()).isEqualTo("TRY");
    }

    @Test
    void ayni_para_biriminde_cikarma_yapar() {
        Money a = new Money(new BigDecimal("100"), "TRY");
        Money b = new Money(new BigDecimal("30"), "TRY");

        assertThat(a.subtract(b).getAmount()).isEqualByComparingTo("70");
    }

    @Test
    void farkli_para_birimleri_toplanamaz() {
        Money tryMoney = new Money(new BigDecimal("100"), "TRY");
        Money usdMoney = new Money(new BigDecimal("50"), "USD");

        assertThatThrownBy(() -> tryMoney.add(usdMoney))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Farkli para birimleri");
    }

    @Test
    void negatif_para_olusturulamaz() {
        assertThatThrownBy(() -> new Money(new BigDecimal("-10"), "TRY"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("negatif");
    }

    @Test
    void ayni_deger_esittir() {
        Money a = new Money(new BigDecimal("100"), "TRY");
        Money b = new Money(new BigDecimal("100"), "TRY");

        assertThat(a).isEqualTo(b);
    }
}
