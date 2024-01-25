INSERT INTO users (username, email, password) VALUES ('user1', 'user1@example.com', 'password1');
INSERT INTO users (username, email, password) VALUES ('user2', 'user2@example.com', 'password2');

INSERT INTO categories (name) VALUES ('Food');
INSERT INTO categories (name) VALUES ('Transport');

INSERT INTO transaction (user_id, date, amount, category_id, description) VALUES (1, '2024-01-10', 50.0, 1, 'Grocery shopping');
INSERT INTO transaction (user_id, date, amount, category_id, description) VALUES (1, '2024-01-15', 20.0, 2, 'Bus ticket');

INSERT INTO recurring_transaction (user_id, amount, category_id, interval, interval_unit, start_date, next_transaction_date, end_date, description) VALUES (1, 100.0, 1, 1, 'Months', '2024-01-01', '2024-02-01', '2024-12-31', 'Monthly grocery shopping');