-- liquibase formatted sql

-- changeset nikoir:002-insert-ref-data
-- comment: Заполнение справочников

-- Валюты
INSERT INTO currencies (code, name, symbol) VALUES
('USD', 'US Dollar', '$'),
('RUB', 'Russian Ruble', '₽'),
('EUR', 'Euro', '€');

-- Периоды бюджета
INSERT INTO budget_periods (code, name, description) VALUES
('DAY', 'День', 'Лимит на один день'),
('WEEK', 'Неделя', 'Лимит на 7 дней с начала периода'),
('MONTH', 'Месяц', 'Лимит на календарный месяц'),
('YEAR', 'Год', 'Лимит на календарный год'),
('CUSTOM', 'Произвольный', 'Ручной выбор дат');