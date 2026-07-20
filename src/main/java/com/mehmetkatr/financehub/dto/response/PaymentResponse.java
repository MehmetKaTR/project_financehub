package com.mehmetkatr.financehub.dto.response;

import com.mehmetkatr.financehub.entity.Payment;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private Long id;
    private Long userId;
    private Long bankAccountId;
    private String toIban;
    private String toName;
    private BigDecimal amount;
    private String currency;
    private Payment.PaymentStatus status;
    private String description;
    private LocalDateTime scheduledDate;
    private LocalDateTime executedAt;
}
