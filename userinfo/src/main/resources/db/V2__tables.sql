CREATE TABLE IF NOT EXISTS "userinfo"."userinfo"(id INTEGER PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255), profile_image VARCHAR(255));

CREATE TABLE IF NOT EXISTS "userinfo"."places"(id SERIAL PRIMARY KEY, name VARCHAR(255), user_id INTEGER, address_id INTEGER, CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES "userinfo"."userinfo"(id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS "userinfo"."report"(id SERIAL PRIMARY KEY, user_id INTEGER, reported_id INTEGER, title VARCHAR(255), category VARCHAR(255), description VARCHAR(1024), CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES "userinfo"."userinfo"(id) ON DELETE CASCADE, CONSTRAINT fk_reported_id FOREIGN KEY(reported_id) REFERENCES "userinfo"."userinfo"(id) ON DELETE CASCADE);