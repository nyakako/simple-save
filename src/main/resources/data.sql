INSERT INTO users (username, email, password) VALUES ('nakamura', 'user1@example.com', '$2a$10$Zl/07QcsTq8G5ufATO3vlua7bI9ePdEZ0Cv21d4DwTFY5heM34thy');
INSERT INTO users (username, email, password) VALUES ('user2', 'user2@example.com', '$2a$10$K5.Hovf3Lvywj50uiOnp5uX7f/iFjTq2lD2bWweg1XQIN1G5UAz7u');

INSERT INTO category (name, type, user_id) VALUES ('食費', 'expense', 1);
INSERT INTO category (name, type, user_id) VALUES ('交通費', 'expense', 1);
INSERT INTO category (name, type, user_id) VALUES ('光熱費', 'expense', 1);
INSERT INTO category (name, type, user_id) VALUES ('日用品', 'expense', 1);
INSERT INTO category (name, type, user_id) VALUES ('給与', 'income', 1);
INSERT INTO category (name, type, user_id) VALUES ('年金', 'income', 1);
INSERT INTO category (name, type, user_id) VALUES ('臨時収入', 'income', 1);
INSERT INTO category (name, type, user_id) VALUES ('ユーザー2の支出カテゴリ1', 'expense', 2);
INSERT INTO category (name, type, user_id) VALUES ('ユーザー2の支出カテゴリ2', 'expense', 2);
INSERT INTO category (name, type, user_id) VALUES ('ユーザー2の収入カテゴリ1', 'income', 2);
INSERT INTO category (name, type, user_id) VALUES ('ユーザー2の収入カテゴリ2', 'income', 2);

INSERT INTO transaction (user_id, date, amount, category_id, description, is_scheduled) VALUES (1, '2024-01-10', 500, 1, 'user1 食料品', false);
INSERT INTO transaction (user_id, date, amount, category_id, description, is_scheduled) VALUES (1, '2024-01-15', 200, 2, 'user1 バス代', false);
INSERT INTO transaction (user_id, date, amount, category_id, description, is_scheduled) VALUES (2, '2024-01-15', 200, 2, 'user2 バス代', false);
INSERT INTO transaction (user_id, date, amount, category_id, description, is_scheduled) VALUES (2, '2024-01-15', 200, 2, 'user2 バス代', false);

INSERT INTO recurring_transaction (user_id, amount, category_id, interval_value, interval_unit, start_date, next_transaction_date, end_date, description) VALUES (1, 100, 1, 1, 'days', '2024-01-01', '2024-02-05', '2024-12-31', '毎日テスト');
INSERT INTO recurring_transaction (user_id, amount, category_id, interval_value, interval_unit, start_date, next_transaction_date, end_date, description, day_of_week) VALUES (1, 100, 1, 1, 'weeks', '2024-01-01', '2024-02-05', '2024-12-31', '毎週テスト','MONDAY');
INSERT INTO recurring_transaction (user_id, amount, category_id, interval_value, interval_unit, start_date, next_transaction_date, end_date, description, day_of_month_monthly) VALUES (1, 100, 1, 1, 'months', '2024-01-01', '2024-02-05', '2024-12-31', '毎月テスト', 5);
INSERT INTO recurring_transaction (user_id, amount, category_id, interval_value, interval_unit, start_date, next_transaction_date, end_date, description,  month_of_year,day_of_month) VALUES (1, 100, 1, 1, 'years', '2024-01-01', '2024-02-05', '2025-12-31', '毎年テスト', 2, 5);
INSERT INTO recurring_transaction (user_id, amount, category_id, interval_value, interval_unit, start_date, next_transaction_date, end_date, description,  month_of_year,day_of_month) VALUES (2, 100, 8, 1, 'years', '2024-01-01', '2024-02-05', '2025-12-31', 'user2テスト', 2, 5);