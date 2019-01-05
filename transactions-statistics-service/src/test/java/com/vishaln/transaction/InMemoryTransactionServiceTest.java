package com.vishaln.transaction;

import com.vishaln.domain.Transaction;
import com.vishaln.service.InMemoryTransactionService;
import com.vishaln.service.StatisticsService;
import mockit.Injectable;
import mockit.StrictExpectations;
import mockit.Tested;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for Transaction Service.
 *
 * @author vishal_nagpure
 */
public class InMemoryTransactionServiceTest {

    @Injectable
    private StatisticsService statisticsService;

    @Tested
    private InMemoryTransactionService inMemoryTransactionService;

    @Test
    public void shouldRecordWhenValidTransactionIsGiven() {

        final Transaction transaction = getTransaction();

        new StrictExpectations() {
            {
                statisticsService.updateStatistics(transaction);
            }
        };

        inMemoryTransactionService.create(transaction);
        assertThat(inMemoryTransactionService.getTransactions(), hasItem(transaction));
    }

    @Test
    public void shouldDeleteAllTransaction() {

        new StrictExpectations() {
            {
                statisticsService.resetStatistics();
            }
        };

        inMemoryTransactionService.deleteAllTransactions();
        assertThat(inMemoryTransactionService.getTransactions(), hasSize(0));
    }

    @Test
    public void shouldEvictTransactionsWhichAreOlderThanOneMinute() throws InterruptedException {

        final Transaction t1 = getTransaction("5", Instant.now().minus(Duration.ofSeconds(55)).toString());
        final Transaction t2 = getTransaction("5", Instant.now().toString());
        final Transaction t3 = getTransaction("5", Instant.now().minus(Duration.ofSeconds(58)).toString());

        inMemoryTransactionService.create(t1);
        inMemoryTransactionService.create(t2);
        inMemoryTransactionService.create(t3);

        TimeUnit.SECONDS.sleep(6);

        inMemoryTransactionService.evictExpiredTransactions();

        assertThat(inMemoryTransactionService.getTransactions(), hasSize(1));
        assertThat(inMemoryTransactionService.getTransactions(), not(contains(t1)));
    }

    @Test
    public void ShouldCallUpdateStatisticsAfterEviction() throws InterruptedException {

        final Transaction t1 = getTransaction("5", Instant.now().minus(Duration.ofSeconds(56)).toString());
        final Transaction t2 = getTransaction("5", Instant.now().toString());
        final Transaction t3 = getTransaction("5", Instant.now().minus(Duration.ofSeconds(58)).toString());

        new StrictExpectations() {
            {
                statisticsService.updateStatistics((Transaction) any);
                times = 3;
            }
        };

        new StrictExpectations() {
            {
                statisticsService.updateStatistics((Queue<Transaction>) any);
            }
        };

        inMemoryTransactionService.create(t1);
        inMemoryTransactionService.create(t2);
        inMemoryTransactionService.create(t3);

        TimeUnit.SECONDS.sleep(6);

        inMemoryTransactionService.evictExpiredTransactions();
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
