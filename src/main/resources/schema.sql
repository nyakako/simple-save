CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255),
    amount DOUBLE,
    date VARCHAR(255),
    category VARCHAR(255)
);
