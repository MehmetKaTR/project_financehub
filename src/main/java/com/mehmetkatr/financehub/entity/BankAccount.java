package com.mehmetkatr.financehub.entity;


import com.mehmetkatr.financehub.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "bank_accounts", indexes = {
        @Index(name = "idx_bank_accounts_user_id", columnList = "user_id")
})
public class BankAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_accounts_seq")
    @SequenceGenerator(name = "bank_accounts_seq", sequenceName = "bank_accounts_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank
    @Column(name="bank_name", nullable = false, length = 100)
    private String bankName;

    @Column(name="bank_account_number", length = 20)
    private String bankAccountNumber;

    @Column(length = 34)
    private String iban;

    @NotBlank
    @Column(length = 3, nullable = false)
    private String currency;

    @NotNull
    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "is_active")
    private boolean isActive = true;

    public enum AccountType {
        CHECKING,
        SAVINGS,
        CREDIT,
        INVESTMENT
    }
}


