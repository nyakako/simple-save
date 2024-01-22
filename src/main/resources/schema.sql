CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    categoryId BIGINT,
    description VARCHAR(255),
    amount DOUBLE,
    date DATE,
    FOREIGN KEY (categoryId) REFERENCES category(id)
);
