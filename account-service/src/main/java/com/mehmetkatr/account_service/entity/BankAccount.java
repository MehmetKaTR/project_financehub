package com.mehmetkatr.account_service.entity;

import com.mehmetkatr.account_service.domain.Money;
import com.mehmetkatr.account_service.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import lombok.*;

import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "bank_accounts", indexes = {
        @Index(name = "idx_bank_accounts_user_id", columnList = "user_id")
})
@SQLDelete(sql = "UPDATE bank_accounts SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false") // HasQueryFilter : sadece false olanlar gelecek
public class BankAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_accounts_seq")
    @SequenceGenerator(name = "bank_accounts_seq", sequenceName = "bank_accounts_seq", allocationSize = 1)
    private Long id;

    // ⭐ ESKİ: @ManyToOne User user  →  YENİ: Long userId
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;              // User NESNESİ DEĞİL — sadece referans ID

    @NotBlank
    @Column(name="bank_name", nullable = false, length = 100)
    private String bankName;

    @Column(name="bank_account_number", length = 20)
    private String bankAccountNumber;

    @Column(length = 34)
    private String iban;

    @NotNull
    @Embedded
    private Money balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public void deposit(Money amount){
        if (!this.isActive) throw new RuntimeException("Bank account is not active");
        if (this.balance.getAmount().compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("Deposit amount must be positive");
        this.balance = this.balance.add(amount);
    }

    public void withdraw(Money amount) {
        if (!this.isActive) throw new RuntimeException("Bank account is not active");
        if (this.balance.getAmount().compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("Withdraw amount must be positive");
        if (balance.getAmount().compareTo(amount.getAmount()) < 0) throw new RuntimeException("Insufficient balance");
        this.balance = this.balance.subtract(amount);
    }

    public Transaction addTransaction(Money amount, Category category, Transaction.TransactionTypes type, String description){
        return Transaction.builder()
                .bankAccount(this)
                .category(category)
                .balance(amount)
                .transactionType(type)
                .description(description).build();
    }

    public enum AccountType { CHECKING, SAVINGS, CREDIT, INVESTMENT }
}