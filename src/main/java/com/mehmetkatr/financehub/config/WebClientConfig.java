package com.mehmetkatr.financehub.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${qnb.api.base-url}")
    private String baseUrl;

    @Value("${qnb.api.token}")
    private String token;

    // QNB Open Banking API'sine istek atmak için kullanılan tekil WebClient bean'i.
    // Tüm istekler otomatik olarak baseUrl ve Authorization header'ı ile gider.
    @Bean
    public WebClient qnbWebClient(){
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
    }

}
