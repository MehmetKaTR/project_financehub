package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.dto.qnb.QnbMoneyTransferRequest;
import com.mehmetkatr.financehub.dto.qnb.QnbMoneyTransferResponse;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.entity.Payment;
import com.mehmetkatr.financehub.repository.BankAccountRepository;
import com.mehmetkatr.financehub.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountService bankAccountService;

    private final QnbApiService qnbApiService;

    private final PaymentRepository paymentRepository;


    public Payment transferMoney(Long accountId, String toIban, String toName,
                                 BigDecimal amount, String currency,
                                 String addressType, String addressValue){

        BankAccount updatedAccount = bankAccountService.withdraw(accountId, amount);

        Payment payment = Payment.builder()
                .user(updatedAccount.getUser())
                .bankAccount(updatedAccount)
                .toIban(toIban != null ? toIban : addressValue)
                .toName(toName)
                .amount(amount)
                .currency(currency)
                .status(Payment.PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        QnbMoneyTransferRequest.QnbMoneyTransferRequestBuilder requestBuilder = QnbMoneyTransferRequest.builder()
                .accountNumber(Long.parseLong(updatedAccount.getBankAccountNumber()))
                .currency(currency)
                .amount(amount.toString())
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
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
        }
        paymentRepository.save(payment);

        return payment;
    }

}
