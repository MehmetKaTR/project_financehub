package com.mehmetkatr.financehub.infrastructure.adapter;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.qnb.QnbStatementResponse;
import com.mehmetkatr.financehub.port.*;
import com.mehmetkatr.financehub.service.QnbApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QnbStatementAdapter implements BankStatementPort {

    private final QnbApiService qnbApiService;

    @Override
    public StatementResult fetchStatement(String iban, LocalDateTime start, LocalDateTime end) {

        QnbStatementResponse resp = qnbApiService.getStatement(iban,start,end);

        List<StatementLine> lines =  resp.getAccountTransactionList().stream()
                .map(dto -> new StatementLine(
                        new Money(new BigDecimal(dto.getTransactionAmount()), dto.getCurrencyCode()),
                        dto.getDebitOrCreditCode(),
                        dto.getTransactionDescription(),
                        dto.getProductOperationRefNo()
                ))
                .toList();

        Money closingMoney = new Money(new BigDecimal(resp.getAccountBalance()),
                resp.getAccountCurrencyCode());

        return new StatementResult(lines,closingMoney);
    }
}