package com.mehmetkatr.payment_service.infrastructure.adapter;

import com.mehmetkatr.payment_service.dto.qnb.QnbMoneyTransferRequest;
import com.mehmetkatr.payment_service.dto.qnb.QnbMoneyTransferResponse;
import com.mehmetkatr.payment_service.port.BankTransferPort;
import com.mehmetkatr.payment_service.port.TransferCommand;
import com.mehmetkatr.payment_service.port.TransferResult;
import com.mehmetkatr.payment_service.service.QnbApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QnbTransferAdapter implements BankTransferPort {

    private final QnbApiService qnbApiService;

    @Override
    public TransferResult transfer(TransferCommand command) {

        QnbMoneyTransferRequest.QnbMoneyTransferRequestBuilder builder = QnbMoneyTransferRequest.builder()
                .accountNumber(Long.parseLong(command.senderAccountNumber()))
                .currency(command.amount().getCurrency())
                .amount(command.amount().getAmount().toString())
                .receiverName(command.receiverName())
                .firmReferansNumber(Long.parseLong(command.firmReference()))
                .rentType("03")            // ← QNB detayı, ARTIK BURADA (PaymentService'te değil)
                .versionNumber("1");       // ← QNB detayı, burada

        if (command.targetIban() != null) {
            builder.targetAccount(command.targetIban());
        } else {
            builder.addressType(command.addressType());
            builder.addressValue(command.addressValue());
        }
        QnbMoneyTransferRequest request = builder.build();

        QnbMoneyTransferResponse response = qnbApiService.transferMoney(request);
        boolean success = "000".equals(response.getResultCode()) || "998".equals(response.getResultCode());

        return new TransferResult(success, response.getSlipNumber());
    }
}
