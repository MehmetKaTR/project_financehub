package com.mehmetkatr.payment_service.event;

import java.math.BigDecimal;

public record PaymentCompletedEvent(
        Long paymentId,
        Long bankAccountId,   // hangi hesaba işlem eklenecek
        BigDecimal amount,
        String currency,
        String toName
) {}