package com.mehmetkatr.financehub.port;

public interface BankTransferPort {
    TransferResult transfer(TransferCommand command);
}
