package com.turing.expensetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExpenseDataAccessException extends RuntimeException {
    public ExpenseDataAccessException(String message) {
        super(message);
    }
}
