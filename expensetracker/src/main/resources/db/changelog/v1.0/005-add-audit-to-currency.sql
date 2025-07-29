-- liquibase formatted sql

-- changeset nikoir:005-add-audit-to-currency
-- comment: Создание поля полей аудита для таблицы currency для отслежвания изменений сущности
ALTER TABLE currencies ADD COLUMN created_by VARCHAR(255);
ALTER TABLE currencies ADD COLUMN created_at TIMESTAMP;
ALTER TABLE currencies ADD COLUMN updated_by VARCHAR(255);
ALTER TABLE currencies ADD COLUMN updated_at TIMESTAMP;
