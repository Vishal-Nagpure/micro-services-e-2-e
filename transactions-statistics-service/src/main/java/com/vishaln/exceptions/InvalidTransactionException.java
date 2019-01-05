package com.vishaln.exceptions;

public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTransactionException(String invaid) {
        super(invaid);
    }
}
