package com.mehmetkatr.payment_service.repository;

import com.mehmetkatr.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);

    List<Payment> findByBankAccountId(Long accountId);

    List<Payment> findByAmountCurrency(String currency);

    List<Payment> findByStatus(Payment.PaymentStatus status);

}
