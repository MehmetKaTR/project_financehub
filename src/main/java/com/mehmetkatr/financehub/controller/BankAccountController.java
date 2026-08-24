package com.mehmetkatr.financehub.controller;

import com.mehmetkatr.financehub.domain.Money;
import com.mehmetkatr.financehub.dto.request.CreateBankAccountRequest;
import com.mehmetkatr.financehub.dto.response.BankAccountResponse;
import com.mehmetkatr.financehub.service.command.BankAccountCommandService;
import com.mehmetkatr.financehub.service.query.BankAccountQueryService;
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

    // private final BankAccountService bankAccountService;
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

        BankAccountResponse bankAccount = bankAccountCommandService.deposit(id, amount);

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<BankAccountResponse> processWithdraw(@PathVariable Long id, @RequestParam BigDecimal amount){

        BankAccountResponse bankAccount = bankAccountCommandService.withdrawForApi(id, amount);

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<BankAccountResponse> activateBankAccount(@PathVariable Long id){

        BankAccountResponse bankAccount = bankAccountCommandService.activateAccount(id);

        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<BankAccountResponse> deactivateBankAccount(@PathVariable Long id){

        BankAccountResponse bankAccount = bankAccountCommandService.deactivateAccount(id);

        return ResponseEntity.ok(bankAccount);
    }

}
