package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.request.TransferMoneyRequest;
import com.mehmetkatr.financehub.dto.response.PaymentResponse;
import com.mehmetkatr.financehub.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/transfer")
    public ResponseEntity<PaymentResponse> transferMoney(@RequestBody TransferMoneyRequest request){

        Money balance = new Money(request.getAmount(), request.getCurrency());

        PaymentResponse payment = paymentService.transferMoney(
                request.getAccountId(),
                request.getToIban(),
                request.getToName(),
                balance,
                request.getAddressType(),
                request.getAddressValue()
        );

        return ResponseEntity.ok(payment);
    }
}
