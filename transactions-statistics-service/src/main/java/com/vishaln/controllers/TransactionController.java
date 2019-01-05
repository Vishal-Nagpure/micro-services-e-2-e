package com.vishaln.controllers;

import com.vishaln.domain.Transaction;
import com.vishaln.service.TransactionService;
import com.vishaln.service.TransactionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller for all the interactions related to transactions. As of now, it supports creation
 * of new transaction and clearing all transactions.
 * <p>
 * Also, the service is designed to delete all the transactions automatically which are older than 60 seconds.
 * So at any given time, transactions for last 60 seconds would be available only.
 *
 * @author vishal_nagpure
 */
@RestController
public class TransactionController {

    private TransactionService transactionService;

    /**
     * Constructor Injection
     *
     * @param transactionService
     */
    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Validates the incoming transaction and adds it to underlying database.
     * Transactions older than 60 seconds will not be accepted. Transaction timestamp
     * is supported in ISO-8601 format.
     *
     * @param transaction
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createTransaction(@Valid @RequestBody final Transaction transaction) {

        // Validate transaction
        TransactionValidator.validate(transaction);

        // Record new transaction by delegating it to underlying service.
        transactionService.create(transaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Delete all transactions.
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/transactions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransactions() {
        transactionService.deleteAllTransactions();
    }
}
