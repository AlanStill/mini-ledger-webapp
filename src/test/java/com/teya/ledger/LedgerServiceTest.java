package com.teya.ledger;

import com.teya.ledger.exceptions.InsufficientFundsException;
import com.teya.ledger.model.TransactionType;
import com.teya.ledger.service.LedgerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class LedgerServiceTest {

    private LedgerService service;

    @BeforeEach
    void setUp() {
        service = new LedgerService();
    }

    @Test
    void deposit_increases_balance() {
        service.deposit(new BigDecimal("100.00"), "salary");
        assertThat(service.getBalance()).isEqualByComparingTo("100.00");
    }

    @Test
    void withdrawal_decreases_balance() {
        service.deposit(new BigDecimal("200.00"), "initial");
        service.withdraw(new BigDecimal("50.00"), "coffee");
        assertThat(service.getBalance()).isEqualByComparingTo("150.00");
    }

    @Test
    void withdrawal_beyond_balance_throws() {
        service.deposit(new BigDecimal("10.00"), "seed");
        assertThatThrownBy(() -> service.withdraw(new BigDecimal("99.00"), "too much"))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void zero_amount_deposit_throws() {
        assertThatThrownBy(() -> service.deposit(BigDecimal.ZERO, "nothing"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void history_records_all_transactions_in_order() {
        service.deposit(new BigDecimal("500"), "A");
        service.withdraw(new BigDecimal("100"), "B");
        service.deposit(new BigDecimal("50"), "C");

        var history = service.getHistory();
        assertThat(history).hasSize(3);
        assertThat(history.get(0).type()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(history.get(1).type()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(history.get(2).type()).isEqualTo(TransactionType.DEPOSIT);
    }

    @Test
    void balance_starts_at_zero() {
        assertThat(service.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
