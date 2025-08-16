package io.github.nikoir.expensetracker.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApiException {
    public ValidationException(String message) {
      super(HttpStatus.BAD_REQUEST, message);
    }
}
