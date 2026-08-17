package com.mehmetkatr.financehub.port;

import java.time.LocalDateTime;

public interface BankStatementPort {
    StatementResult fetchStatement(String iban, LocalDateTime start, LocalDateTime end);
}