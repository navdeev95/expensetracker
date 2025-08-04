-- liquibase formatted sql

-- changeset nikoir:005-delete-user-from-budget
-- comment: Удаление колонки user из таблицы budgets
ALTER TABLE budgets
DROP COLUMN user_id;