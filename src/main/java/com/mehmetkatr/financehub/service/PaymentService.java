package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.qnb.QnbMoneyTransferRequest;
import com.mehmetkatr.financehub.dto.qnb.QnbMoneyTransferResponse;
import com.mehmetkatr.financehub.dto.response.PaymentResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.Payment;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountService bankAccountService;

    private final QnbApiService qnbApiService;

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse transferMoney(Long accountId, String toIban, String toName,
                                         Money amount,
                                         String addressType, String addressValue){

        BankAccount updatedAccount = bankAccountService.withdraw(accountId, amount.getAmount());

        Payment payment = Payment.builder()
                .user(updatedAccount.getUser())
                .bankAccount(updatedAccount)
                .toIban(toIban != null ? toIban : addressValue)
                .toName(toName)
                .amount(amount)
                .status(Payment.PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        QnbMoneyTransferRequest.QnbMoneyTransferRequestBuilder requestBuilder = QnbMoneyTransferRequest.builder()
                .accountNumber(Long.parseLong(updatedAccount.getBankAccountNumber()))
                .currency(amount.getCurrency())
                .amount(amount.getAmount().toString())
                .receiverName(toName)
                .rentType("03")
                .versionNumber("1")
                .firmReferansNumber(payment.getId());

        if (toIban != null) {
            requestBuilder.targetAccount(toIban);
        } else {
            requestBuilder.addressType(addressType);
            requestBuilder.addressValue(addressValue);
        }

        QnbMoneyTransferRequest request = requestBuilder.build();

        QnbMoneyTransferResponse response = qnbApiService.transferMoney(request);

        if ("000".equals(response.getResultCode()) || "998".equals(response.getResultCode())) {
            payment.markAsCompleted();
        } else {
            payment.markAsFailed();
        }
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
