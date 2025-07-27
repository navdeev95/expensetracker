package io.github.nikoir.expensetracker.exception.handler;

import io.github.nikoir.expensetracker.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorAttributes errorAttributes;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException exception, WebRequest request) {
        Map<String, Object> errorMap = errorAttributes.getErrorAttributes(
                request,
                ErrorAttributeOptions.defaults()
        );

        errorMap.replace("status", exception.getCode());
        errorMap.replace("error", exception.getMessage());
        errorMap.put("details", exception.getDetails());

        return ResponseEntity
                .status(exception.getCode())
                .body(errorMap);
    }

}
