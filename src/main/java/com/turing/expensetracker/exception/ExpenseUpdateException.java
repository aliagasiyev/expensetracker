package com.turing.expensetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExpenseUpdateException extends RuntimeException {
    public ExpenseUpdateException(String message) {
        super(message);
    }
}
