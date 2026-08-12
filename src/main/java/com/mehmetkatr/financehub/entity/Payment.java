package com.mehmetkatr.financehub.entity;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_user_id", columnList = "user_id"),
        @Index(name = "idx_payments_status", columnList = "status"),
        @Index(name = "idx_payments_scheduled_date", columnList = "scheduled_date")
})
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payments_seq")
    @SequenceGenerator(name = "payments_seq", sequenceName = "payments_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="from_account_id",  nullable=false)
    private BankAccount bankAccount;

    @NotBlank
    @Column(name="to_iban", length=34, nullable=false)
    private String toIban;

    @NotBlank
    @Column(name="to_name", length=100, nullable=false)
    private String toName;

    @NotNull
    @Embedded
    private Money amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private PaymentStatus status;

    @Column(length=255, nullable=true)
    private String description;

    @Column(name="scheduled_date",nullable=true)
    private LocalDateTime scheduledDate;

    @Column(name="executed_at",nullable=true)
    private LocalDateTime executedAt;

    public void markAsCompleted(){
        this.status = PaymentStatus.COMPLETED;
    }

    public void markAsFailed(){
        this.status = PaymentStatus.FAILED;
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELED
    }
}