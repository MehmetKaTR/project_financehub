package com.mehmetkatr.account_service.controller;

import com.mehmetkatr.account_service.domain.Money;
import com.mehmetkatr.account_service.dto.request.CreateBankAccountRequest;
import com.mehmetkatr.account_service.dto.response.BankAccountResponse;
import com.mehmetkatr.account_service.service.command.BankAccountCommandService;
import com.mehmetkatr.account_service.service.query.BankAccountQueryService;
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

    private final BankAccountQueryService bankAccountQueryService;
    private final BankAccountCommandService bankAccountCommandService;

    @PostMapping
    public ResponseEntity<BankAccountResponse> createBankAccount(@RequestBody CreateBankAccountRequest request){
        Money balance = new Money(request.getBalance(), request.getCurrency());

        BankAccountResponse newBankAccount = bankAccountCommandService.createBankAccount(
                request.getUserId(),
                request.getBankName(),
                request.getBankAccountNumber(),
                request.getIban(),
                balance,
                request.getAccountType()
        );

        return ResponseEntity.ok(newBankAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponse> getAccountInfoById(@PathVariable Long id) {
        Optional<BankAccountResponse> bankAccount = bankAccountQueryService.findById(id);
        return bankAccount.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BankAccountResponse>> getAccountInfoByUserId(@PathVariable Long userId){
        List<BankAccountResponse> bankAccounts = bankAccountQueryService.findByUserId(userId);
        return ResponseEntity.ok(bankAccounts);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<BankAccountResponse> processDeposit(@PathVariable Long id, @RequestParam BigDecimal amount){
        return ResponseEntity.ok(bankAccountCommandService.deposit(id, amount));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<BankAccountResponse> processWithdraw(@PathVariable Long id, @RequestParam BigDecimal amount){
        return ResponseEntity.ok(bankAccountCommandService.withdrawForApi(id, amount));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<BankAccountResponse> activateBankAccount(@PathVariable Long id){
        return ResponseEntity.ok(bankAccountCommandService.activateAccount(id));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<BankAccountResponse> deactivateBankAccount(@PathVariable Long id){
        return ResponseEntity.ok(bankAccountCommandService.deactivateAccount(id));
    }
}
