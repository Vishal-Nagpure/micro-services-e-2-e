package com.vishaln.transaction;

import com.vishaln.domain.Transaction;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Unit tests for POJO Transaction
 *
 * @author vishal_nagpure
 */
public class TransactionTest {

    @Test
    public void shouldReturnTrueWhenTransactionIsExpired() {
        assertThat(getTransaction("12.12", Instant.now().minus(Duration.ofMinutes(2)).toString()).isExpired(), is(true));
    }

    @Test
    public void shouldReturnFalseWhenTransactionIsActive() {
        assertThat(getTransaction("12.12", Instant.now().toString()).isExpired(), is(false));

    }

    private Transaction getTransaction(final String amount, final String timestamp) {
        return Transaction.builder().amount(amount).timestamp(timestamp).build();
    }
}
