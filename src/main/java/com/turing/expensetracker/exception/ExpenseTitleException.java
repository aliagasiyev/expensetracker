package com.turing.expensetracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExpenseTitleException extends RuntimeException {
    public ExpenseTitleException(String message) {
        super(message);
    }
}
