package com.mehmetkatr.financehub.entity;

import com.mehmetkatr.financehub.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Table(name = "budgets", indexes = {
        @Index(name = "idx_budgets_user_id", columnList = "user_id"),
        @Index(name = "idx_budgets_user_category", columnList = "user_id, category_id")
})
public class Budget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "budgets_seq")
    @SequenceGenerator(name = "budgets_seq", sequenceName = "budgets_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PeriodType period;

    @Max(12)
    @Min(1)
    @Column(name = "month", nullable = true) //precision in integer == lenght in string
    private Integer month;

    @NotNull
    @Max(2100)
    @Min(2000)
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "limit_amount", precision = 19, scale = 4, nullable = false)
    private BigDecimal limitAmount;

    @NotNull
    @Column(name = "spend_amount",  precision = 19, scale = 4, nullable = false)
    private BigDecimal spendAmount;

    public enum PeriodType {
        YEARLY,
        MONTHLY,
        DAILY
    }
}

