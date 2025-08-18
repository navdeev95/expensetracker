-- liquibase formatted sql

-- changeset nikoir:001-insert-test-data
-- comment: Вставка тестовых данных

INSERT INTO USERS(ID) VALUES('bf005a04-26b6-4a8a-9427-46ae6997962d');
        INSERT INTO CATEGORIES(ID, USER_ID, NAME, DESCRIPTION)
        VALUES(1, 'bf005a04-26b6-4a8a-9427-46ae6997962d', 'Еда', 'Расходы на еду');

INSERT INTO BUDGETS(CATEGORY_ID, PERIOD_CODE, AMOUNT, START_DATE, END_DATE, CURRENCY_CODE, IS_ACTIVE)
VALUES (1, 'MONTH', 30000,
PARSEDATETIME('2025-01-01 00:00:00', 'yyyy-MM-dd HH:mm:ss'),
PARSEDATETIME('2025-01-31 23:59:59', 'yyyy-MM-dd HH:mm:ss'),
'RUB', false);