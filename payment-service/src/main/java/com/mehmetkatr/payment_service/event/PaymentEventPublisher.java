package com.mehmetkatr.payment_service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {

    private final KafkaTemplate kafkaTemplate;

    public void publishPaymentCompleted(PaymentCompletedEvent event){
        kafkaTemplate.send("payment-events", event);
    }
}
