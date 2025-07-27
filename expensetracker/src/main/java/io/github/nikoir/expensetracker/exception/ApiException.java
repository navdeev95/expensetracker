package io.github.nikoir.expensetracker.exception;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.Map;

@RequiredArgsConstructor
@Getter
@Setter
public class ApiException extends RuntimeException {
    private final int code;
    private final String message;
    private final Map<String, String> details;
}
