package com.teya.ledger.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teya.ledger.model.BalanceResponse;
import com.teya.ledger.model.Transaction;
import com.teya.ledger.model.TransactionRequest;
import com.teya.ledger.model.TransactionType;
import com.teya.ledger.service.LedgerService;

@RestController
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping("/transactions")
    public String recordTransaction(@RequestBody TransactionRequest request) {
        TransactionType type = request.type();

        Transaction tx = switch (type) {
            case DEPOSIT -> ledgerService.deposit(request.amount(), request.description());
            case WITHDRAWAL -> ledgerService.withdraw(request.amount(), request.description());
        };

        return tx.id();
    }

    @GetMapping("/balance")
    public BalanceResponse getBalance() {
        return new BalanceResponse(ledgerService.getBalance());
    }

    @GetMapping("/transactions")
    public List<Transaction> getHistory() {
        List<Transaction> transactions = ledgerService.getHistory();

        List<Transaction> history = transactions.stream()
                .sorted(Comparator.comparing(Transaction::createdAt).reversed())
                .toList();
        return history;
    }

}
