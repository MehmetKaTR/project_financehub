package com.mehmetkatr.account_service.event;

import java.math.BigDecimal;

public record PaymentCompletedEvent(
        Long paymentId,
        Long bankAccountId,
        BigDecimal amount,
        String currency,
        String toName
) {}