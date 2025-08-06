-- liquibase formatted sql

-- changeset nikoir:007-add-is-active-to-budget
-- comment: Добавление в таблицу бюджетов поля is_active (для архивации данных)
ALTER TABLE budgets
ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;