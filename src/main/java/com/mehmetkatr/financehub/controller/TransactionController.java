package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.dto.request.TransactionRequest;
import com.mehmetkatr.financehub.entity.Transaction;
import com.mehmetkatr.financehub.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionRequest request){

        Transaction newTransaction = transactionService.createTransaction(
                request.getBankAccountId(),
                request.getCategoryId(),
                request.getAmount(),
                request.getCurrency(),
                request.getTransactionTypes(),
                request.getDescription()
        );

        return ResponseEntity.ok(newTransaction);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getByAccountId(@PathVariable Long accountId) {

        List<Transaction> transaction = transactionService.findByBankAccountId(accountId);

        return ResponseEntity.ok(transaction);
    }


}
