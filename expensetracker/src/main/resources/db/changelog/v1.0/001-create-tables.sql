-- liquibase formatted sql

-- changeset nikoir:001-create-tables
-- comment: Создание основных таблиц

-- Пользователи
CREATE TABLE users (
    id varchar(36) PRIMARY KEY,

    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Категории расходов
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    name VARCHAR(50) NOT NULL,
    description TEXT,

    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL,
    UNIQUE (user_id, name)
);

-- Валюты (справочник)
CREATE TABLE currencies (
    code VARCHAR(3) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    symbol VARCHAR(5),

    version BIGINT NOT NULL DEFAULT 0,

    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Периоды бюджета (справочник)
CREATE TABLE budget_periods (
    code VARCHAR(20) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description TEXT,

    version BIGINT NOT NULL DEFAULT 0,

    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Расходы
CREATE TABLE expenses (
    id BIGSERIAL PRIMARY KEY,
    user_id varchar(36) NOT NULL,
    category_id BIGINT,
    currency_code VARCHAR(3) DEFAULT 'RUB',
    amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
    description TEXT,
    date DATE NOT NULL DEFAULT CURRENT_DATE,

    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    FOREIGN KEY (currency_code) REFERENCES currencies(code),
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Бюджеты
CREATE TABLE budgets (
    id BIGSERIAL PRIMARY KEY,
    user_id varchar(36) NOT NULL,
    category_id BIGINT,
    period_code VARCHAR(20) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_by VARCHAR(36),
    updated_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    FOREIGN KEY (period_code) REFERENCES budget_periods(code),

    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL,

    CHECK (end_date > start_date)
);