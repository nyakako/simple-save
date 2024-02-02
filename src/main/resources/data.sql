INSERT INTO category (name, type) VALUES ('食費', 'expense');
INSERT INTO category (name, type) VALUES ('交通費', 'expense');
INSERT INTO category (name, type) VALUES ('光熱費', 'expense');
INSERT INTO category (name, type) VALUES ('日用品', 'expense');
INSERT INTO category (name, type) VALUES ('給与', 'income');
INSERT INTO category (name, type) VALUES ('年金', 'income');
INSERT INTO category (name, type) VALUES ('臨時収入', 'income');

INSERT INTO users (username, email, password) VALUES ('user1', 'user1@example.com', 'password1');
INSERT INTO users (username, email, password) VALUES ('user2', 'user2@example.com', 'password2');

INSERT INTO transaction (user_id, date, amount, category_id, description, is_scheduled) VALUES (1, '2024-01-10', 500, 1, '食料品', false);
INSERT INTO transaction (user_id, date, amount, category_id, description, is_scheduled) VALUES (1, '2024-01-15', 200, 2, 'バス代', false);

INSERT INTO recurring_transaction (user_id, amount, category_id, interval_value, interval_unit, start_date, next_transaction_date, end_date, description) VALUES (1, 100, 1, 1, 'days', '2024-01-01', '2024-02-02', '2024-12-31', 'ジュース');