package com.vishaln.exceptions;

public class OlderTransactionException extends RuntimeException {

    public OlderTransactionException() {

    }

    public OlderTransactionException(final String message) {
        super(message);
    }
}
