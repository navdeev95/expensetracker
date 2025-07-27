package io.github.nikoir.expensetracker.exception;

import io.github.nikoir.expensetracker.service.EntityType;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

public class AlreadyExistsException extends ApiException {
    public AlreadyExistsException(EntityType entity, Object id, Map<String, String> details) {
        super(HttpStatus.CONFLICT, String.format("Сущность %s с id = %s уже существует", entity, id), details);
    }
    public AlreadyExistsException(EntityType entity, Object id) {
        this(entity, id, Collections.emptyMap());
    }

    public AlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message, Collections.emptyMap());
    }
}
