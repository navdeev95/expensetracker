package io.github.nikoir.expensetracker.exception;

import io.github.nikoir.expensetracker.service.EntityType;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;

public class NotFoundException extends ApiException {
    public NotFoundException(EntityType entity, Object id, Map<String, String> details) {
        super(HttpStatus.NOT_FOUND, String.format("Не найдена сущность %s с id = %s", entity, id), details);
    }
    public NotFoundException(EntityType entity, Object id) {
        this(entity, id, Collections.emptyMap());
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
