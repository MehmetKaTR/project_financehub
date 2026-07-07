package com.mehmetkatr.financehub.dto.qnb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QnbStatementResponse {

    private String resultCode;
    private String resultDescription;

    private String accountNo;
    private String iban;
    private String accountTitle;
    private String accountType;
    private String accountCurrencyCode;
    private String accountBalance;

    private String openingBalance;
    private String openingBalanceDate;

    private String branchCode;
    private String branchName;
    private String customerNo;

    private List<QnbTransactionDto> accountTransactionList;
}
