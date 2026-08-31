package com.mehmetkatr.payment_service.infrastructure.adapter;

import com.mehmetkatr.payment_service.dto.qnb.QnbMoneyTransferRequest;
import com.mehmetkatr.payment_service.dto.qnb.QnbMoneyTransferResponse;
import com.mehmetkatr.payment_service.port.BankTransferPort;
import com.mehmetkatr.payment_service.port.TransferCommand;
import com.mehmetkatr.payment_service.port.TransferResult;
import com.mehmetkatr.payment_service.service.QnbApiService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QnbTransferAdapter implements BankTransferPort {

    private final QnbApiService qnbApiService;

    @Override
    @CircuitBreaker(name = "qnb", fallbackMethod = "transferFallback")
    public TransferResult transfer(TransferCommand command) {

        QnbMoneyTransferRequest.QnbMoneyTransferRequestBuilder builder = QnbMoneyTransferRequest.builder()
                .accountNumber(Long.parseLong(command.senderAccountNumber()))
                .currency(command.amount().getCurrency())
                .amount(command.amount().getAmount().toString())
                .receiverName(command.receiverName())
                .firmReferansNumber(Long.parseLong(command.firmReference()))
                .rentType("03")
                .versionNumber("1");

        if (command.targetIban() != null) {
            builder.targetAccount(command.targetIban());
        }
        else {
            builder.addressType(command.addressType());
            builder.addressValue(command.addressValue());
        }

        QnbMoneyTransferRequest request = builder.build();
        QnbMoneyTransferResponse response = qnbApiService.transferMoney(request);
        boolean success = "000".equals(response.getResultCode()) || "998".equals(response.getResultCode());

        return new TransferResult(success, response.getSlipNumber());
    }

    public TransferResult transferFallback(TransferCommand command, Throwable t) {
        System.out.println("QNB circuit breaker fallback: " + t.getMessage());
        return new TransferResult(false, null);
    }
}
