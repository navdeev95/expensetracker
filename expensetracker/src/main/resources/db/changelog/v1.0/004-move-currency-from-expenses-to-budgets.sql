-- liquibase formatted sql

-- changeset nikoir:004-move-currency-from-expenses-to-budgets
-- comment: Перенос currency_code из expenses в budgets

ALTER TABLE budgets
ADD COLUMN currency_code VARCHAR(3) NOT NULL DEFAULT 'RUB';

ALTER TABLE budgets
ADD CONSTRAINT fk_budgets_currency
FOREIGN KEY (currency_code) REFERENCES currencies(code);

ALTER TABLE expenses
DROP CONSTRAINT IF EXISTS expenses_currency_code_fkey;

ALTER TABLE expenses
DROP COLUMN currency_code;