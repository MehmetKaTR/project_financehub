package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.response.PaymentResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.Payment;
import com.mehmetkatr.financehub.port.BankTransferPort;
import com.mehmetkatr.financehub.port.TransferCommand;
import com.mehmetkatr.financehub.port.TransferResult;
import com.mehmetkatr.financehub.repository.PaymentRepository;
import com.mehmetkatr.financehub.service.command.BankAccountCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PaymentService {

    // private final BankAccountService bankAccountService;
    private final BankAccountCommandService bankAccountCommandService;

    private final BankTransferPort bankTransferPort;

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse transferMoney(Long accountId, String toIban, String toName,
                                         Money amount,
                                         String addressType, String addressValue){

        BankAccount updatedAccount = bankAccountCommandService.withdraw(accountId, amount.getAmount());

        Payment payment = Payment.builder()
                .user(updatedAccount.getUser())
                .bankAccount(updatedAccount)
                .toIban(toIban != null ? toIban : addressValue)
                .toName(toName)
                .amount(amount)
                .status(Payment.PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        TransferCommand command = new TransferCommand(
                updatedAccount.getBankAccountNumber(),
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

    // convert Payment to PaymentResponse
    private PaymentResponse toResponse(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setUserId(payment.getUser().getId());
        response.setBankAccountId(payment.getBankAccount().getId());
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
