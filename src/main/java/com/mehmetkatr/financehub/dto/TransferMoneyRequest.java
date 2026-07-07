package com.mehmetkatr.financehub.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferMoneyRequest {
    private Long accountId;
    private String toIban;
    private String toName;
    private BigDecimal amount;
    private String currency;
    private String addressType;
    private String addressValue;
}
