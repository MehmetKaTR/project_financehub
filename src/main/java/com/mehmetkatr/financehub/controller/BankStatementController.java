package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.service.TransactionSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankStatementController {

     private final TransactionSyncService transactionSyncService;

     @PostMapping("/{id}/sync")
     public ResponseEntity<String> sync(
         @PathVariable Long id,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
         LocalDate date) {

         int count = transactionSyncService.syncDay(id, date);
         return ResponseEntity.ok(count + " islem senkronize edildi");

     }
}