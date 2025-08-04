-- liquibase formatted sql

-- changeset nikoir:006-replace-text-to-varchar-in-budget-period
-- comment: Замена типа колонки description c text на varchar(255) в таблице budget_periods
ALTER TABLE budget_periods
ALTER COLUMN description TYPE varchar(255);