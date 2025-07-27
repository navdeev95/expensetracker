package io.github.nikoir.expensetracker.exception;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@Setter
public class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String message;
    private final Map<String, String> details;
}
