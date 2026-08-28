package com.mehmetkatr.account_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events", indexes = {
        @Index(name = "idx_outbox_processed", columnList = "processed")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outbox_event_seq")
    @SequenceGenerator(name = "outbox_event_seq", sequenceName = "outbox_event_seq", allocationSize = 1)
    private Long id;

    @Column(nullable=false)
    private String aggregateType;   // "BankAccount" — hangi tür değişti

    @Column(nullable=false)
    private Long aggregateId;       // hangi kayıt (hesap id'si)

    @Column(nullable=false)
    private String eventType;       // "CHANGED" — ne oldu

    @Column(nullable=false)
    private boolean processed;      // işlendi mi? (poller bunu kontrol eder)

    private LocalDateTime createdAt;
}
