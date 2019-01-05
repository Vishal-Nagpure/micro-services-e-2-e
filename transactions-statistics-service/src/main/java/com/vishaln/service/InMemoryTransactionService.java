package com.vishaln.service;

import com.vishaln.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

@Service
@EnableScheduling
public class InMemoryTransactionService implements TransactionService {

    private static final int INITIAL_CAPACITY = 100;
    private final Queue<Transaction> transactions = new PriorityBlockingQueue<>(INITIAL_CAPACITY, Comparator.comparing(Transaction::getTimestamp));
    private StatisticsService statisticsService;

    @Autowired
    public InMemoryTransactionService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Override
    public void create(Transaction transaction) {
        transactions.offer(transaction);
        updateStatistics(transaction);
    }

    /**
     * Runs at the interval of 1 millisecond and delete all the transactions
     * which are older than one second.
     */
    @Scheduled(fixedRate = 1)
    public void evictExpiredTransactions() {
        while (!transactions.isEmpty() && transactions.peek().isExpired()) {
            transactions.poll();
        }
        statisticsService.updateStatistics(transactions);
    }

    private void updateStatistics(final Transaction transaction) {

        statisticsService.updateStatistics(transaction);
    }

    @Override
    public void deleteAllTransactions() {
        statisticsService.resetStatistics();
        transactions.clear();
    }

    public Queue<Transaction> getTransactions() {
        return transactions;
    }
}
