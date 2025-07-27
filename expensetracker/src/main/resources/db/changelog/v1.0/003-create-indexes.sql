-- liquibase formatted sql

-- changeset nikoir:003-create-indexes
-- comment: Создание индексов

-- Для ускорения поиска расходов по пользователю и дате
CREATE INDEX idx_expenses_user_date ON expenses(user_id, date);

-- Для проверки бюджетов
CREATE INDEX idx_budgets_user_period ON budgets(user_id, period_code, start_date, end_date);