package com.vishaln.exceptions;

public class FutureTransactionException extends RuntimeException {

    public FutureTransactionException() {
    }

    public FutureTransactionException(String message) {
        super(message);
    }
}
