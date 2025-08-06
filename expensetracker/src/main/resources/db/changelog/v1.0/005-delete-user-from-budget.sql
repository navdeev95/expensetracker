-- liquibase formatted sql

-- changeset nikoir:005-delete-user-from-budget
-- comment: Удаление колонки user из таблицы budgets
DROP INDEX IF EXISTS idx_budgets_user_period;

ALTER TABLE budgets
DROP COLUMN IF EXISTS user_id;

CREATE INDEX idx_budgets_category_period ON budgets(category_id, period_code, start_date, end_date);


