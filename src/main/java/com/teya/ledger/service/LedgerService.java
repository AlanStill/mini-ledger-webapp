package com.teya.ledger.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.teya.ledger.exceptions.InsufficientFundsException;
import com.teya.ledger.model.Transaction;
import com.teya.ledger.model.TransactionType;

@Service
public class LedgerService {
    
private final List<Transaction> transactions = new ArrayList<>();

public Transaction deposit(BigDecimal amount, String description) {
        validatePositive(amount);
        Transaction tx = Transaction.of(TransactionType.DEPOSIT, amount, description);
        transactions.add(tx);
        return tx;
    }
 
    public Transaction withdraw(BigDecimal amount, String description) {
        validatePositive(amount);

        BigDecimal balance = getBalance();
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException(amount, balance);
        }
        Transaction tx = Transaction.of(TransactionType.WITHDRAWAL, amount, description);
        transactions.add(tx);
        return tx;
    }
 
    /**
     * Calculate balance on demand by replaying all transactions. 
     * @return current balance 
     */
    public BigDecimal getBalance() {
        BigDecimal balance = BigDecimal.ZERO;

    for (Transaction tx : transactions) {
        if (tx.type() == TransactionType.DEPOSIT) {
            balance = balance.add(tx.amount());
        } else {
            balance = balance.subtract(tx.amount());
        }
    }

        return balance;
    }
 
    public List<Transaction> getHistory() {
        return Collections.unmodifiableList(transactions);
    }
 
    private void validatePositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

}
