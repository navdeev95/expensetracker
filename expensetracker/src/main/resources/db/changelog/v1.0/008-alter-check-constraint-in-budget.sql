-- liquibase formatted sql

-- changeset nikoir:008-alter-check-constraint-in-budget
-- comment: Изменение логики проверки end_date и start_date у бюджета
ALTER TABLE budgets DROP CONSTRAINT budgets_check;

ALTER TABLE budgets ADD CONSTRAINT budgets_check CHECK (end_date >= start_date);