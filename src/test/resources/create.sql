CREATE TABLE if not exists managers
(
    id UUID PRIMARY KEY ,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE if not exists clients
(
    id UUID PRIMARY KEY ,
    interest_rate DOUBLE,
    status VARCHAR(50),
    tax_code VARCHAR(20),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(60),
    address VARCHAR(80),
    phone VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    manager_id UUID,
    FOREIGN KEY (manager_id) REFERENCES managers(id)
);

CREATE TABLE if not exists accounts
(
    id            uuid PRIMARY KEY,
    client_id     uuid,
    account_name  VARCHAR(100),
    password  VARCHAR(100),
    account_type  VARCHAR(30),
    status        VARCHAR(50),
    balance       DECIMAL(15, 2),
    currency_code VARCHAR(3),
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES clients (id)
);

CREATE TABLE if not exists cards
(
    card_id UUID PRIMARY KEY,
    card_number VARCHAR(25),
    card_holder VARCHAR(25),
    cvv VARCHAR(3),
    card_type VARCHAR(30),
    card_status VARCHAR(30),
    expiration_date TIMESTAMP,
    created_at TIMESTAMP,
    account_id UUID,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE if not exists products
(
    id UUID PRIMARY KEY,
    name VARCHAR(70),
    status VARCHAR(50),
    currency_code VARCHAR(3),
    interest_rate DECIMAL(6,4),
    limitt INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    manager_id UUID,
    FOREIGN KEY (manager_id) REFERENCES managers(id)
);

CREATE TABLE if not exists agreements
(
    id            UUID PRIMARY KEY ,
    interest_rate DECIMAL(6, 3),
    status        VARCHAR(50),
    sum           DECIMAL(15, 2),
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    account_id    UUID,
    product_id    UUID,
    FOREIGN KEY (account_id) REFERENCES accounts (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE if not exists transactions
(
    id UUID PRIMARY KEY,
    transaction_type VARCHAR(20),
    amount DECIMAL(12,2),
    description VARCHAR(255),
    status VARCHAR(50),
    created_at TIMESTAMP,
    debit_account_id UUID,
    credit_account_id UUID,
    FOREIGN KEY (debit_account_id) REFERENCES accounts(id),
    FOREIGN KEY (credit_account_id) REFERENCES accounts(id)
);