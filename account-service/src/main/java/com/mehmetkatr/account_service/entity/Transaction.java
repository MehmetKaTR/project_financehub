package com.mehmetkatr.account_service.entity;

import com.mehmetkatr.account_service.domain.Money;
import com.mehmetkatr.account_service.entity.base.JCreatedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_transactions_bank_account_id", columnList = "bank_account_id"),
        @Index(name = "idx_transactions_category_id", columnList = "category_id"),
        @Index(name = "idx_transactions_created_at", columnList = "created_at")
})
public class Transaction extends JCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transactions_seq")
    @SequenceGenerator(name = "transactions_seq", sequenceName = "transactions_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;          // iç entity → @ManyToOne KALIR

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;                // iç entity → @ManyToOne KALIR

    @NotNull
    @Embedded
    private Money balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="transaction_type", nullable = false)
    private TransactionTypes transactionType;

    @Column(length = 255, nullable = true)
    private String description;

    @Column(name="reference_number", length = 100, nullable = true)
    private String referenceNumber;

    public enum TransactionTypes { INCOME, EXPENSE, TRANSFER }
}