package com.mehmetkatr.financehub.port;

import com.mehmetkatr.financehub.domain.Money;
import java.util.List;

public record StatementResult(
        List<StatementLine> lines,
        Money closingBalance
) {}