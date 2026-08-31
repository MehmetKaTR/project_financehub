package com.mehmetkatr.payment_service.client;

import com.mehmetkatr.payment_service.dto.response.BankAccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "account-service")
public interface AccountClient {

    @PostMapping("/api/bank-accounts/{id}/withdraw")
    BankAccountResponse withdraw(@PathVariable Long id, @RequestParam BigDecimal amount);

    @PostMapping("/api/bank-accounts/{id}/deposit")           // ← YENİ (telafi)
    BankAccountResponse deposit(@PathVariable Long id, @RequestParam BigDecimal amount);
}
