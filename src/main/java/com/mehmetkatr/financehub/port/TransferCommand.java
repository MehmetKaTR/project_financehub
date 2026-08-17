package com.mehmetkatr.financehub.port;

import com.mehmetkatr.financehub.domain.Money;

// Record: Sadece veri taşımak için kullanılan sınıflarda boilerplate kodu azaltmak için normal class yerine kullanılır.
// Java'da sadece "veri taşıyan" immutable sınıflar için record var. class + @Data yerine tek satır. Command bir "veri paketi" olduğu için record ideal. İstersen normal @Data class de yapabilirsin, ama record daha modern/temiz.
public record TransferCommand(
        String senderAccountNumber,
        String targetIban,
        Money amount,
        String receiverName,
        String addressType,
        String addressValue,
        String firmReference
) {}