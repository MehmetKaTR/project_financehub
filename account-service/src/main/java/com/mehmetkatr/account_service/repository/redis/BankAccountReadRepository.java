package com.mehmetkatr.account_service.repository.redis;

import com.mehmetkatr.account_service.service.query.model.BankAccountReadModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BankAccountReadRepository extends CrudRepository<BankAccountReadModel, Long> {
    List<BankAccountReadModel> findByUserId(Long userId);
    List<BankAccountReadModel> findByBankName(String bankName);
    List<BankAccountReadModel> findByAccountType(String accountType);
}
