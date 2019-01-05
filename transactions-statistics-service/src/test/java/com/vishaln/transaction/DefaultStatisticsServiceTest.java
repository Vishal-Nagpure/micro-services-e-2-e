package com.vishaln.transaction;

import com.vishaln.domain.Statistic;
import com.vishaln.domain.Transaction;
import com.vishaln.service.DefaultStatisticsService;
import mockit.Tested;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * Unit tests for Statistics Service
 *
 * @author vishal_nagpure
 */
public class DefaultStatisticsServiceTest {

    @Tested
    private DefaultStatisticsService defaultStatisticsService;

    @Test
    public void shouldUpdateStatisticsForSingleTransaction() {

        // Given
        final Transaction transaction = getTransaction();

        // When
        defaultStatisticsService.updateStatistics(transaction);

        // Assert
        defaultStatisticsService.getStatistics().equals(getExpectedStatistics("12.54", "12.54", "12.54", "12.54", 1));
    }

    @Test
    public void shouldUpdateStatisticsForBulkTransaction() {

        final Transaction t1 = getTransaction("5", Instant.now().minus(Duration.ofSeconds(5)).toString());
        final Transaction t2 = getTransaction("3", Instant.now().toString());
        final Transaction t3 = getTransaction("4", Instant.now().minus(Duration.ofSeconds(8)).toString());

        final Queue<Transaction> transactions = new PriorityBlockingQueue<>(10, Comparator.comparing(Transaction::getTimestamp));

        transactions.offer(t1);
        transactions.offer(t2);
        transactions.offer(t3);

        defaultStatisticsService.updateStatistics(transactions);

        defaultStatisticsService.getStatistics().equals(getExpectedStatistics("3", "5", "4", "12", 3));
    }

    @Test
    public void shouldResetStatistics() {

        final Transaction t1 = getTransaction("5", Instant.now().minus(Duration.ofSeconds(5)).toString());
        final Transaction t2 = getTransaction("3", Instant.now().toString());
        final Transaction t3 = getTransaction("4", Instant.now().minus(Duration.ofSeconds(8)).toString());

        final Queue<Transaction> transactions = new PriorityBlockingQueue<>(10, Comparator.comparing(Transaction::getTimestamp));

        transactions.offer(t1);
        transactions.offer(t2);
        transactions.offer(t3);

        defaultStatisticsService.updateStatistics(transactions);
        assertThat((int) defaultStatisticsService.getStatistics().getCount(), equalTo(3));

        defaultStatisticsService.resetStatistics();
        assertThat((int) defaultStatisticsService.getStatistics().getCount(), equalTo(0));
    }

    private Statistic getExpectedStatistics(String min, String max, String avg, String sum, int count) {
        return Statistic.builder()
                .count(count)
                .avg(new BigDecimal(avg))
                .max(new BigDecimal(max))
                .min(new BigDecimal(min))
                .sum(new BigDecimal(sum))
                .build();
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

    public static void main(String[] args) {
        System.out.println(Instant.now());
    }
}
