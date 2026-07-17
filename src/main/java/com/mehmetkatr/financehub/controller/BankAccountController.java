package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.dto.request.CreateBankAccountRequest;
import com.mehmetkatr.financehub.entity.BankAccount;
import com.mehmetkatr.financehub.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody CreateBankAccountRequest request){
        BankAccount newBankAccount = bankAccountService.createBankAccount(
                request.getUserId(),
                request.getBankName(),
                request.getBankAccountNumber(),
                request.getIban(),
                request.getCurrency(),
                request.getBalance(),
                request.getAccountType()
        );

        return ResponseEntity.ok(newBankAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getAccountInfoById(@PathVariable Long id) {
        Optional<BankAccount> bankAccount = bankAccountService.findById(id);

        return bankAccount.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BankAccount>> getAccountInfoByUserId(@PathVariable Long userId){

        List<BankAccount> bankAccounts = bankAccountService.findByUserId(userId);

        return ResponseEntity.ok(bankAccounts);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<BankAccount> processDeposit(@PathVariable Long id, @RequestParam BigDecimal amount){

        BankAccount bankAccount = bankAccountService.deposit(id, amount);

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<BankAccount> processWithdraw(@PathVariable Long id, @RequestParam BigDecimal amount){

        BankAccount bankAccount = bankAccountService.withdraw(id, amount);

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<BankAccount> activateBankAccount(@PathVariable Long id){

        BankAccount bankAccount = bankAccountService.activateAccount(id);

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<BankAccount> deactivateBankAccount(@PathVariable Long id){

        BankAccount bankAccount = bankAccountService.deactivateAccount(id);

        return ResponseEntity.ok(bankAccount);
    }

}
