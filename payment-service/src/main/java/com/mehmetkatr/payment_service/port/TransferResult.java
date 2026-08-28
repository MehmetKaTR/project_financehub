package com.mehmetkatr.payment_service.port;

public record TransferResult(
        boolean success,
        String reference   // slipNumber/inquiryNumber — takip için
) {}
