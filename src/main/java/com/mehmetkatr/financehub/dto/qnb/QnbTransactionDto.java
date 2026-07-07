package com.mehmetkatr.financehub.dto.qnb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QnbTransactionDto {

    private String transactionId;
    private String productOperationRefNo;

    private String transactionDate;
    private String valueDate;

    private String transactionAmount;
    private String balanceAfterTransaction;
    private String currencyCode;

    private String debitOrCreditCode;
    private String transactionDescription;
    private String processCode;

    private String opponentIBAN;
    private String opponentBank;
}
