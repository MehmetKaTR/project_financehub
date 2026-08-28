package com.mehmetkatr.payment_service.service;

import com.mehmetkatr.payment_service.client.AccountClient;
import com.mehmetkatr.payment_service.domain.Money;
import com.mehmetkatr.payment_service.dto.response.BankAccountResponse;
import com.mehmetkatr.payment_service.dto.response.PaymentResponse;
import com.mehmetkatr.payment_service.entity.Payment;
import com.mehmetkatr.payment_service.port.BankTransferPort;
import com.mehmetkatr.payment_service.port.TransferCommand;
import com.mehmetkatr.payment_service.port.TransferResult;
import com.mehmetkatr.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final AccountClient accountClient;          // Feign (CommandService yerine)
    private final BankTransferPort bankTransferPort;    // QNB portu (aynı)
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse transferMoney(Long accountId, String toIban, String toName,
                                         Money amount,
                                         String addressType, String addressValue){

        // Feign: account-service'e HTTP ile "bakiye düş" (DTO döner)
        BankAccountResponse updatedAccount = accountClient.withdraw(accountId, amount.getAmount());

        Payment payment = Payment.builder()
                .userId(updatedAccount.getUserId())         // userId
                .bankAccountId(updatedAccount.getId())      // bankAccountId
                .toIban(toIban != null ? toIban : addressValue)
                .toName(toName)
                .amount(amount)
                .status(Payment.PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        TransferCommand command = new TransferCommand(
                updatedAccount.getBankAccountNumber(),      // DTO'dan (aynı)
                toIban != null ? toIban : addressValue,
                amount,
                toName,
                addressType,
                addressValue,
                Long.toString(payment.getId())
        );

        TransferResult resp = bankTransferPort.transfer(command);
        if(resp.success())
            payment.markAsCompleted();
        else
            payment.markAsFailed();

        paymentRepository.save(payment);

        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setUserId(payment.getUserId());               // getUserId
        response.setBankAccountId(payment.getBankAccountId()); // getBankAccountId
        response.setToIban(payment.getToIban());
        response.setToName(payment.getToName());
        response.setAmount(payment.getAmount().getAmount());
        response.setCurrency(payment.getAmount().getCurrency());
        response.setStatus(payment.getStatus());
        response.setDescription(payment.getDescription());
        response.setScheduledDate(payment.getScheduledDate());
        response.setExecutedAt(payment.getExecutedAt());
        return response;
    }
}