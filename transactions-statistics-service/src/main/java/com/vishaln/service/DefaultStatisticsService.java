package com.vishaln.service;

import com.vishaln.domain.Statistic;
import com.vishaln.domain.Transaction;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
public class DefaultStatisticsService implements StatisticsService {

    private volatile Statistic statistics = new Statistic();

    @Override
    public synchronized void updateStatistics(final Queue<Transaction> transactions) {
        statistics = statistics.compute(transactions);
    }

    @Override
    public synchronized Statistic getStatistics() {
        return statistics;
    }

    @Override
    public synchronized void resetStatistics() {
        statistics.reset();
    }

    @Override
    public synchronized void updateStatistics(final Transaction transaction) {
        statistics = statistics.update(transaction);
    }
}
