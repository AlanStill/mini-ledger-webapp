package com.teya.ledger.model;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

/**
 * Client Contract for a Transaction
 */
public record TransactionRequest(
       
        @NotNull TransactionType type,
        @NotNull BigDecimal amount,
        String description)
         {
    public String description() {
        return description != null ? description : "";
    }
    
}
