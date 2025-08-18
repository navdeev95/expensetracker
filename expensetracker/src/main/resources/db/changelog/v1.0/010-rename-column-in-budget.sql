-- liquibase formatted sql

-- changeset nikoir:010-rename-column-in-budget
-- comment: Переименование колонки is_active в is_deleted

ALTER TABLE budgets
RENAME COLUMN is_active TO is_deleted;

ALTER TABLE budgets
ALTER COLUMN is_deleted SET DEFAULT false;