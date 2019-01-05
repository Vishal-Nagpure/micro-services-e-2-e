package com.vishaln.service;

import com.vishaln.domain.Statistic;
import com.vishaln.domain.Transaction;

import java.util.Queue;

/**
 * Contract for Statistics
 *
 * @author vishal_nagpure
 */
public interface StatisticsService {

    /**
     * It should compute and update the statistics based on provided list of transaction.
     *
     * @param transactions list of transction
     */
    void updateStatistics(final Queue<Transaction> transactions);

    /**
     * Returns the available statistics.
     *
     * @return
     */
    Statistic getStatistics();

    /**
     * Reset statistics if any.
     */
    void resetStatistics();

    /**
     * Update statistics data only with current transaction. Currently it supports updation of statistics
     * for new transactions only.
     * <p>
     * Removal of one transaction cannot update statistics for previous as of now.
     * See method updateStatistics for same
     *
     * @param transaction
     */
    void updateStatistics(final Transaction transaction);
}
