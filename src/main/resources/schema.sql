CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    color_preference VARCHAR(255) DEFAULT 'greenPositive',
    CONSTRAINT users_email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS category (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL CHECK (type IN ('income', 'expense')),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS recurring_transaction (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    description VARCHAR(255),
    amount NUMERIC(19,2) NOT NULL,
    interval_value INT NOT NULL DEFAULT 1,
    interval_unit VARCHAR(255) NOT NULL,
    day_of_week VARCHAR(255),
    day_of_month_monthly INT,
    day_of_month INT,
    month_of_year INT,
    start_date DATE NOT NULL,
    end_date DATE,
    next_transaction_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE IF NOT EXISTS transaction (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    category_id BIGINT NOT NULL,
    description VARCHAR(255),
    is_scheduled BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);
