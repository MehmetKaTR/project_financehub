package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.dto.request.CreateBankAccountRequest;
import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
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
    public ResponseEntity<BankAccountResponse> createBankAccount(@RequestBody CreateBankAccountRequest request){
        BankAccountResponse newBankAccount = bankAccountService.createBankAccount(
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
    public ResponseEntity<BankAccountResponse> getAccountInfoById(@PathVariable Long id) {
        Optional<BankAccountResponse> bankAccount = bankAccountService.findById(id);

        return bankAccount.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BankAccountResponse>> getAccountInfoByUserId(@PathVariable Long userId){

        List<BankAccountResponse> bankAccounts = bankAccountService.findByUserId(userId);

        return ResponseEntity.ok(bankAccounts);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<BankAccountResponse> processDeposit(@PathVariable Long id, @RequestParam BigDecimal amount){

        BankAccountResponse bankAccount = bankAccountService.deposit(id, amount);

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<BankAccountResponse> processWithdraw(@PathVariable Long id, @RequestParam BigDecimal amount){

        BankAccountResponse bankAccount = bankAccountService.withdrawForApi(id, amount);

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<BankAccountResponse> activateBankAccount(@PathVariable Long id){

        BankAccountResponse bankAccount = bankAccountService.activateAccount(id);

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<BankAccountResponse> deactivateBankAccount(@PathVariable Long id){

        BankAccountResponse bankAccount = bankAccountService.deactivateAccount(id);

        return ResponseEntity.ok(bankAccount);
    }

}
