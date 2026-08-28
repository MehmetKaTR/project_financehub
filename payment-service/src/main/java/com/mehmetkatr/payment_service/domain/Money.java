package com.mehmetkatr.payment_service.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;

@Embeddable //Veritabanında money diye ayrı tablo olmaz. Bunun yerine bank_accounts tablosuna iki kolon eklenir: balance_amount | balance_currency
@Getter
public class Money {

    BigDecimal amount;
    String currency;

    protected Money() {
        this.amount = null;
        this.currency = null;
    }

    public Money(BigDecimal amount, String currency){
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("Amount ve currency null olamaz");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Para negatif olamaz");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Farkli para birimleri toplanamaz");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Farkli para birimleri cikarilamaz");
        }
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public boolean isGreaterThan(Money other)
    {
        if(!this.currency.equals(other.currency))
        {
            throw new IllegalArgumentException("Farkli para birimleri karsilastirilamaz");
        }
        return this.amount.compareTo(other.amount) > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0 && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(amount, currency);
    }

}
