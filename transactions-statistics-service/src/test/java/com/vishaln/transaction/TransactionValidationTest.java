package com.vishaln.transaction;

import com.vishaln.domain.Transaction;
import com.vishaln.exceptions.FutureTransactionException;
import com.vishaln.exceptions.InvalidTransactionException;
import com.vishaln.exceptions.OlderTransactionException;
import com.vishaln.service.TransactionValidator;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;

/**
 * Unit Tests for Transaction Validation.
 *
 * @author vishal_nagpure
 */
public class TransactionValidationTest {

    @Test
    public void shouldSucceedWhenValidTransactionIsGiven() {
        TransactionValidator.validate(getTransaction());
    }

    @Test(expectedExceptions = FutureTransactionException.class)
    public void shouldThrowExceptionForFutureTransactions() {
        TransactionValidator.validate(getTransaction("5", Instant.now().plus(Duration.ofMinutes(5)).toString()));
    }

    @Test(expectedExceptions = OlderTransactionException.class)
    public void shouldThrowExceptionForTransactionWhichAreOlderThanOneMinute() {
        TransactionValidator.validate(getTransaction("5", Instant.now().minus(Duration.ofMinutes(2)).toString()));
    }

    @Test(expectedExceptions = InvalidTransactionException.class)
    public void shouldThrowExceptionWhenTimestampHasInvalidFormat() {
        TransactionValidator.validate(getTransaction("5", "some-invalid-timestamp"));
    }

    @Test(expectedExceptions = InvalidTransactionException.class)
    public void shouldThrowExceptionWhenAmountIsInvalid() {
        TransactionValidator.validate(getTransaction("Five", Instant.now().toString()));
    }

    private Transaction getTransaction() {

        return Transaction.builder()
                .amount("12.54")
                .timestamp(Instant.now().toString())
                .build();
    }

    private Transaction getTransaction(final String amount, final String timestamp) {

        return Transaction.builder().amount(amount).timestamp(timestamp).build();
    }
}
