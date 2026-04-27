package com.teya.ledger.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


/**
 * Immutable record representing a single money movement.
 */

public record Transaction(
        String id,
        TransactionType type,
        BigDecimal amount,
        String description,
        Instant createdAt) 
{
    public static Transaction of(TransactionType type, BigDecimal amount, String description) {
    return new Transaction(
        UUID.randomUUID().toString(),
        type,
        amount,
        description,
        Instant.now()
    );
}
}