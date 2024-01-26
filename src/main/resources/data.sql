INSERT INTO category (name, type) VALUES ('食費', 'expence');
INSERT INTO category (name, type) VALUES ('交通費', 'expence');

INSERT INTO users (username, email, password) VALUES ('user1', 'user1@example.com', 'password1');
INSERT INTO users (username, email, password) VALUES ('user2', 'user2@example.com', 'password2');

INSERT INTO transaction (user_id, date, amount, category_id, description) VALUES (1, '2024-01-10', 500, 1, '食料品');
INSERT INTO transaction (user_id, date, amount, category_id, description) VALUES (1, '2024-01-15', 200, 2, 'バス代');

INSERT INTO recurring_transaction (user_id, amount, category_id, interval_value, interval_unit, start_date, next_transaction_date, end_date, description) VALUES (1, 100.0, 1, 1, 'Months', '2024-01-01', '2024-02-01', '2024-12-31', 'Monthly grocery shopping');