package com.teya.ledger.exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException{

      public InsufficientFundsException(BigDecimal requested, BigDecimal available) {
        super("Cannot withdraw %s — available balance is %s".formatted(requested, available));
    }
}
