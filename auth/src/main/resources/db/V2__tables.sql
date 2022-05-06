CREATE TABLE IF NOT EXISTS "user"(id SERIAL PRIMARY KEY, email VARCHAR(255) UNIQUE, password VARCHAR(255), phone_number VARCHAR(12), is_verified BOOLEAN);
CREATE TABLE IF NOT EXISTS "role"(user_id INT REFERENCES "user", role VARCHAR(255));