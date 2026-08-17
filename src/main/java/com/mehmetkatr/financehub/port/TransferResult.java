package com.mehmetkatr.financehub.port;

public record TransferResult(
        boolean success,
        String reference   // slipNumber/inquiryNumber — takip için
) {}