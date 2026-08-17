package com.mehmetkatr.financehub.port;

import com.mehmetkatr.financehub.domain.Money;

public record StatementLine(
    Money amount,
    String debitOrCreditCode,
    String transactionDescription,
    String referenceNumber
) {}
