package com.mehmetkatr.payment_service.service;

import com.mehmetkatr.payment_service.dto.qnb.QnbMoneyTransferRequest;
import com.mehmetkatr.payment_service.dto.qnb.QnbMoneyTransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class QnbApiService {

    private final WebClient qnbWebClient;        // WebClientConfig'te tanımladığımız bean
    private final ObjectMapper objectMapper;     // JSON ↔ nesne çevirici (Spring otomatik verir)

    public QnbMoneyTransferResponse transferMoney(QnbMoneyTransferRequest request) {
        String rawJson = qnbWebClient.post()          // POST isteği
                .uri("/v0/money-transfer")             // QNB transfer endpoint'i
                .bodyValue(request)                    // request DTO'yu JSON gövdeye koy
                .retrieve()                            // gönder
                .bodyToMono(String.class)              // cevabı al (reaktif sarmal)
                .block();                              // BEKLE, düz String yap

        return objectMapper.readValue(rawJson, QnbMoneyTransferResponse.class);  // JSON → DTO
    }
}