package com.vishaln.service;

import com.vishaln.domain.Transaction;

/**
 * Contract the needs to be supported by implementations of Transaction service.
 * By Default, it comes with in memory implementation which is based on Queue data structure.
 *
 * @author vishal_nagpure
 */
public interface TransactionService {

    /**
     * Record new transaction in database.
     *
     * @param transaction
     */
    void create(final Transaction transaction);

    /**
     * Delete all transactions.
     */
    void deleteAllTransactions();
}
