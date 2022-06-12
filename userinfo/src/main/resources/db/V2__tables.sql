CREATE TABLE IF NOT EXISTS "userinfo"."userinfo"(id INTEGER PRIMARY KEY, first_name VARCHAR(255), last_name VARCHAR(255), profile_image VARCHAR(255));

CREATE TABLE IF NOT EXISTS "userinfo"."places"(id SERIAL PRIMARY KEY, name VARCHAR(255), first_line VARCHAR(255) NOT NULL, second_line VARCHAR(255), city VARCHAR(255) NOT NULL, state VARCHAR(255) NOT NULL, country VARCHAR(255) NOT NULL, zipcode CHAR(6) NOT NULL, user_id INTEGER, CONSTRAINT fk_user_id FOREIGN KEY(user_id) REFERENCES "userinfo"."userinfo"(id) ON DELETE CASCADE);

