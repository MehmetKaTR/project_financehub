package com.mehmetkatr.financehub.dto.response;

import com.mehmetkatr.financehub.entity.Transaction;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionResponse {

    private Long id;
    private Long bankAccountId;
    private Long categoryId;
    private BigDecimal amount;
    private String currency;
    private Transaction.TransactionTypes type;
    private String description;
    private String referenceNumber;
}
