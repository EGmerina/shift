
CREATE TABLE sellers
(
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(100) NOT NULL
        CONSTRAINT chk_seller_name_length CHECK (length(name) BETWEEN 2 AND 100),
    contact_info      VARCHAR(255) NOT NULL
        CONSTRAINT chk_seller_contact_length CHECK (length(contact_info) BETWEEN 5 AND 255),
    registration_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_deleted        BOOLEAN      NOT NULL    DEFAULT FALSE
);

COMMENT ON TABLE sellers IS 'Таблица для хранения информации о продавцах';
COMMENT ON COLUMN sellers.id IS 'Уникальный идентификатор продавца';
COMMENT ON COLUMN sellers.name IS 'Имя продавца';
COMMENT ON COLUMN sellers.contact_info IS 'Контактные данные (телефон, email и т.д.)';
COMMENT ON COLUMN sellers.registration_date IS 'Дата и время регистрации продавца';
COMMENT ON COLUMN sellers.is_deleted IS 'Флаг мягкого удаления (true - удален, false - активен)';



CREATE TABLE transactions
(
    id               BIGSERIAL PRIMARY KEY,
    seller_id        BIGINT         NOT NULL REFERENCES sellers (id),
    amount           NUMERIC(15, 2) NOT NULL
        CONSTRAINT chk_transaction_amount CHECK (amount > 0),
    payment_type     VARCHAR(20)    NOT NULL
        CONSTRAINT chk_payment_type CHECK (payment_type IN ('CASH', 'CARD', 'TRANSFER')),
    transaction_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX idx_transactions_seller_id ON transactions (seller_id);

CREATE INDEX idx_transactions_date ON transactions (transaction_date);

COMMENT ON TABLE transactions IS 'Таблица для хранения транзакций продавцов';
COMMENT ON COLUMN transactions.id IS 'Уникальный идентификатор транзакции';
COMMENT ON COLUMN transactions.seller_id IS 'Идентификатор продавца, совершившего транзакцию';
COMMENT ON COLUMN transactions.amount IS 'Сумма транзакции (строго больше нуля)';
COMMENT ON COLUMN transactions.payment_type IS 'Тип оплаты (CASH, CARD, TRANSFER)';
COMMENT ON COLUMN transactions.transaction_date IS 'Дата и время совершения транзакции';