package com.mehmetkatr.financehub.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.math.BigDecimal;

@Embeddable
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
}
