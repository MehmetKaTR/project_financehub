package com.mehmetkatr.payment_service.port;

public interface BankTransferPort {
    TransferResult transfer(TransferCommand command);
}
