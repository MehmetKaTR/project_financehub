package com.mehmetkatr.payment_service.port;

import com.mehmetkatr.payment_service.domain.Money;

public record TransferCommand(
        String senderAccountNumber,
        String targetIban,
        Money amount,
        String receiverName,
        String addressType,
        String addressValue,
        String firmReference
) {}
