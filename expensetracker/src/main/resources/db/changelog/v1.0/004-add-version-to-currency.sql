-- liquibase formatted sql

-- changeset nikoir:004-add-version-to-currency
-- comment: Создание поля version для таблицы currency для отслеживания оптимистичных блокировок

ALTER TABLE currencies ADD COLUMN version BIGINT NOT NULL DEFAULT 0;