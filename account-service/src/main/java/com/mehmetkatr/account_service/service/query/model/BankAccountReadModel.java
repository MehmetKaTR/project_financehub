package com.mehmetkatr.account_service.service.query.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@RedisHash("BankAccount")
public class BankAccountReadModel implements Serializable {
    @Id
    private Long id;

    @Indexed
    private Long userId;

    @Indexed
    private String bankName;

    private String iban;
    private BigDecimal balance;
    private String currency;
    private String bankAccountNumber;

    @Indexed
    private String accountType;   // String — Redis'te enum yerine düz string basit

    private boolean active;

    // @Id      → kaydın anahtarı; findById için (otomatik, her zaman çalışır)
    // @Indexed → o alanda arama yapabilmek için ekstra index; findByX metotları için şart
}
