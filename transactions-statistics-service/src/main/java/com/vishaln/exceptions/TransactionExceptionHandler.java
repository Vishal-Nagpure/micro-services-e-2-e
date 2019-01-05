package com.vishaln.exceptions;

import com.vishaln.domain.TransactionApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OlderTransactionException.class)
    public ResponseEntity<Object> invalidTransaction(OlderTransactionException e, WebRequest request) {

        return buildResponseEntity(e, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler({FutureTransactionException.class, InvalidTransactionException.class})
    public ResponseEntity<Object> futureTransaction(Exception e, WebRequest request) {

        return buildResponseEntity(e, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private ResponseEntity buildResponseEntity(final Exception e,
                                               final HttpStatus status) {

        return new ResponseEntity(TransactionApiErrorResponse.builder().httpStatus(status).cause(e.getLocalizedMessage()).build(), status);
    }
}
