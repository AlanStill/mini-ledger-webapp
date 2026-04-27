# Ledger API

A lightweight in-memory ledger built with Spring Boot, with Java 17 and Maven. Spring.io was used to create source template and development in VS Code.

## Notes

This README was originally generated with the assistance of AI. Reviewed and updated manually for accuracy.

## Running

```bash
./mvnw spring-boot:run
```

Server starts at `http://localhost:8080`.

## API

### Record a transaction
```bash
# Deposit
curl -s -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"type":"DEPOSIT","amount":500.00,"description":"salary"}'

# Withdrawal
curl -s -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"type":"WITHDRAWAL","amount":49.99,"description":"groceries"}'
```

### View balance
```bash
curl -s http://localhost:8080/balance
```
```json
{
  "balance" : 450.01
}
```

### View transaction history
```bash
curl -s http://localhost:8080/transactions
```
Returns an array ordered newest-first.

### Error cases
```bash
# Overdraft → 422 Unprocessable Entity
curl -s -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"type":"WITHDRAWAL","amount":9999}'

# Bad input → 400 Bad Request
curl -s -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"type":"DEPOSIT","amount":-1}'
```

## Running tests
```bash
./mvnw test
```

---

## Assumptions

| # | Assumption | Rationale |
|---|-----------|-----------|
| 1 | Ledger is modelled as a single account | Application simplicity, and not explicity requested in the spec.  |
| 2 | Balance calculation favours correctness over speed | Trade off in Read performance accepted, as this can be improved upon with later application development.  |
| 3 | Negative balances not allowed | Standard ledger behaviour. Overdraft feature out of scope. |
| 4 | Amounts must be > 0 | Zero-value transactions have no meaning will generate an exception to inform clients. |
| 5 | Timestamps are server-side UTC | Prevents clock-skew bugs if clients are in different timezones. |
| 6 | History returned newest-first | Convention is to show recent activity at the top. |
| 7 | `description` is optional | Spec doesn't mention it; added because it's standard ledger metadata. |
| 8 | Data is ephemeral | Spec explicitly recommends in-memory storage. Restart clears all data. |

## Project structure

```
src/main/java/com/teya/ledger/
├── LedgerApplication.java          Spring Boot Entry point
├── controller/
│   ├── LedgerController.java       HTTP mapping to handle requests — no logic
├── service/
│   └── LedgerService.java          All business rules + in-memory state
├── model/
│   ├── BalanceRecord.java          Balance Response Object
│   ├── Transaction.java            Immutable record of money movements
│   └── TransactionType.java        DEPOSIT | WITHDRAWAL
│   └── TransactionRequest.java     Client Transaction Contract  
└── exceptions/
    ├── InsufficientFundsException.java
    └── GlobalExceptionHandler.java  Maps exceptions → HTTP responses
```