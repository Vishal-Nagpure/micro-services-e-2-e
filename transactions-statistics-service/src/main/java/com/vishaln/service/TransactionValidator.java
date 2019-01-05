package com.vishaln.service;

import com.vishaln.domain.Transaction;
import com.vishaln.exceptions.FutureTransactionException;
import com.vishaln.exceptions.InvalidTransactionException;
import com.vishaln.exceptions.OlderTransactionException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Responsible for performing all the validations related to transactions.
 *
 * @author vishal_nagpure
 */
public class TransactionValidator {

    public static void validate(final Transaction transaction) {

        if (!isValidIsoDateTime(transaction.getTimestamp())) {
            throw new InvalidTransactionException("The timestamp is not in ISO-8601 format.");
        }

        if (!isValidAmount(transaction.getAmount())) {
            throw new InvalidTransactionException("Please provide a valid amount.");
        }

        final Instant instant = Instant.parse(transaction.getTimestamp());

        if (isOlderTransaction(instant)) {
            throw new OlderTransactionException("Transaction which are older than 60 seconds are not allowed and will be rejected.");
        }

        if (isFutureTransaction(instant)) {
            throw new FutureTransactionException("Future transactions are not allowed.");
        }
    }

    private static boolean isValidAmount(String amount) {
        try {
            new BigDecimal(amount);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean isValidIsoDateTime(String date) {
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isFutureTransaction(final Instant transaction) {
        return transaction.isAfter(Instant.now());
    }

    private static boolean isOlderTransaction(final Instant transaction) {
        final Instant oneMinuteAgo = Instant.now().minus(Duration.ofMinutes(1));
        return transaction.isBefore(oneMinuteAgo);
    }
}
