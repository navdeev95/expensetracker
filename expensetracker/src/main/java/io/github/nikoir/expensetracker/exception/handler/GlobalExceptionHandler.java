package io.github.nikoir.expensetracker.exception.handler;

import io.github.nikoir.expensetracker.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;
import java.util.stream.Collectors;

//TODO: добавить логирование error при ошибках (возможно через AOP)
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorAttributes errorAttributes;

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException exception, WebRequest request) {
        Map<String, Object> errorMap = getErrorAttributes(request);

        errorMap.replace("status", exception.getHttpStatus().value());
        errorMap.replace("error", exception.getMessage());
        if (!CollectionUtils.isEmpty(exception.getDetails())) {
            errorMap.put("details", exception.getDetails());
        }

        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(errorMap);
    }

    //обработчик для ошибок валидации DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception,
                                                                         WebRequest request) {
        Map<String, Object> errorMap = getErrorAttributes(request);

        //Это просто жопа
        Map<String, String> fieldErrors = exception.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                fieldError -> StringUtils.hasText(fieldError.getDefaultMessage())
                                        ? fieldError.getDefaultMessage()
                                        : "Некорректное значение",
                                Collectors.joining("; ")
                        )
                ));

        errorMap.replace("status", HttpStatus.BAD_REQUEST.value());
        errorMap.replace("error", "Ошибка валидации");
        errorMap.put("details", fieldErrors);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMap);

    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, Object>> handleOptimisticLockException(ObjectOptimisticLockingFailureException exception, WebRequest request) {
        Map<String, Object> errorMap = getErrorAttributes(request);

        errorMap.replace("status", HttpStatus.CONFLICT.value());
        errorMap.replace("error", "Данные были обновлены другим пользователем. Пожалуйста обновите страницу и попробуйте снова");

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorMap);
    }

    private Map<String, Object> getErrorAttributes(WebRequest request) {
        return errorAttributes.getErrorAttributes(
                request,
                ErrorAttributeOptions.defaults()
        );
    }

}
