package com.mehmetkatr.account_service.event;

import com.mehmetkatr.account_service.domain.Money;
import com.mehmetkatr.account_service.entity.BankAccount;
import com.mehmetkatr.account_service.entity.Transaction;
import com.mehmetkatr.account_service.repository.BankAccountRepository;
import com.mehmetkatr.account_service.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.kafka.autoconfigure.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.awaitility.Awaitility.await;

// Sadece Kafka + listener'i yukluyoruz. classes={...} verince Spring FULL autoconfig'i
// (Oracle, JPA, Redis, Flyway) CALISTIRMAZ -> altyapi gerekmez, test hafif kalir.
@SpringBootTest(
        classes = { KafkaAutoConfiguration.class, PaymentEventListener.class },
        properties = {
                // embedded broker'in adresini kafka'ya bagla
                "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
                // consumer ayarlari (gercek account-service ile ayni format)
                "spring.kafka.consumer.group-id=account-test-group",
                "spring.kafka.consumer.auto-offset-reset=earliest",
                "spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer",
                "spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer",
                "spring.kafka.consumer.properties.spring.json.trusted.packages=*",
                "spring.kafka.consumer.properties.spring.json.use.type.headers=false",
                "spring.kafka.consumer.properties.spring.json.value.default.type=com.mehmetkatr.account_service.event.PaymentCompletedEvent",
                // producer ayarlari (payment-service nasil gonderiyorsa oyle)
                "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
                "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer"
        }
)
@EmbeddedKafka(topics = "payment-events", partitions = 1)
class PaymentEventKafkaIntegrationTest {

    // embedded kafka'ya mesaj GONDERMEK icin (payment-service rolu)
    @Autowired
    KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    // DB katmani yok -> repository'leri sahte koyuyoruz
    @MockitoBean
    BankAccountRepository bankAccountRepository;
    @MockitoBean
    TransactionRepository transactionRepository;

    @Test
    void mesaj_gonderilince_listener_transaction_kaydeder() {
        // 1) mock DB: id=1 sorulursa aktif bir hesap don ("hesap var" dunyasi)
        BankAccount hesap = BankAccount.builder().id(1L).userId(5L)
                .balance(new Money(new BigDecimal("900"), "TRY"))
                .accountType(BankAccount.AccountType.CHECKING).isActive(true).build();
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(hesap));

        // 2) GERCEKTEN topic'e mesaj at (unit test'te elle cagiriyorduk, burada Kafka tetikliyor)
        PaymentCompletedEvent event =
                new PaymentCompletedEvent(99L, 1L, new BigDecimal("100"), "TRY", "Ahmet");
        kafkaTemplate.send("payment-events", event);

        // 3) listener asenkron calisir -> en fazla 10 sn bekle, save cagrilana kadar
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() ->
                verify(transactionRepository).save(any(Transaction.class))
        );
    }
}
