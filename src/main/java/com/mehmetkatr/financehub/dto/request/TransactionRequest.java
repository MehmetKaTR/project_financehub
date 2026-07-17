package com.mehmetkatr.financehub.dto.request;

import com.mehmetkatr.financehub.entity.Transaction;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    private Long bankAccountId;
    private Long categoryId;
    private BigDecimal amount;
    private String currency;
    private Transaction.TransactionTypes transactionTypes;
    private String description;
}
