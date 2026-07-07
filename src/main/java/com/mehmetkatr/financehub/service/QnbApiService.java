package com.mehmetkatr.financehub.service;

import com.mehmetkatr.financehub.dto.qnb.QnbMoneyTransferRequest;
import com.mehmetkatr.financehub.dto.qnb.QnbMoneyTransferResponse;
import com.mehmetkatr.financehub.dto.qnb.QnbStatementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class QnbApiService {

    private final WebClient qnbWebClient;
    private final ObjectMapper objectMapper;

    private static final DateTimeFormatter QNB_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public QnbStatementResponse getStatement(String iban,
                                             LocalDateTime start,
                                             LocalDateTime end) {
        String rawJson = qnbWebClient.get()
                .uri(b -> b.path("/v0/account-statement")
                        .queryParam("iban", iban)
                        .queryParam("startDateTime", start.format(QNB_FORMAT))
                        .queryParam("endDateTime", end.format(QNB_FORMAT))
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return objectMapper.readValue(rawJson, QnbStatementResponse.class);
    }

    public QnbMoneyTransferResponse transferMoney(QnbMoneyTransferRequest request) {

        String rawJson = qnbWebClient.post()
                .uri("/v0/money-transfer")
                .bodyValue(request)              // ← tüm DTO'yu JSON body olarak gönder
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return objectMapper.readValue(rawJson, QnbMoneyTransferResponse.class);
    }


}
