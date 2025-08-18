-- liquibase formatted sql

-- changeset nikoir:009-alter-dates-in-budget
-- comment: Изменение типов данных у столбцов start_date и end_date

ALTER TABLE budgets
ALTER COLUMN start_date TYPE timestamp;

ALTER TABLE budgets
ALTER COLUMN end_date TYPE timestamp;