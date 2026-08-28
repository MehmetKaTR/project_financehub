package com.mehmetkatr.payment_service.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BankAccountResponse {
    private Long id;
    private Long userId;
    private String bankAccountNumber;
    private BigDecimal balance;
    private String currency;
    // ihtiyacın olan alanlar; account-service'in JSON'undaki isimlerle AYNI olmalı
}