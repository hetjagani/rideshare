CREATE TABLE IF NOT EXISTS "ride"."address"(id SERIAL PRIMARY KEY, street VARCHAR(255), line VARCHAR(255), city VARCHAR(255), state VARCHAR(255), country VARCHAR(255), zipcode VARCHAR(10), lat FLOAT, long FLOAT);

CREATE TABLE IF NOT EXISTS "ride"."tags"(id SERIAL PRIMARY KEY, name VARCHAR(255));

CREATE TABLE IF NOT EXISTS "ride"."ride"(id SERIAL PRIMARY KEY, post_id INTEGER, user_id INTEGER, created_at TIMESTAMP, price_per_person FLOAT, no_passengers INTEGER, capacity INTEGER, status VARCHAR(255), start_address INTEGER, end_address INTEGER, started_at TIMESTAMP, ended_at TIMESTAMP, ride_time TIMESTAMP, CONSTRAINT fk_address_id FOREIGN KEY(start_address) REFERENCES "ride"."address"(id) ON DELETE NO ACTION, CONSTRAINT fk_address_id1 FOREIGN KEY(end_address) REFERENCES "ride"."address"(id) ON DELETE NO ACTION);

CREATE TABLE IF NOT EXISTS "ride"."ride_tags"(ride_id INTEGER, tag_id INTEGER, CONSTRAINT fk_ride_id FOREIGN KEY (ride_id) REFERENCES "ride"."ride"(id) ON DELETE CASCADE, CONSTRAINT fk_tag_id FOREIGN KEY(tag_id) REFERENCES "ride"."tags"(id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS "ride"."request"(id SERIAL PRIMARY KEY, user_id INTEGER, ride_id INTEGER, stripe_payment_id VARCHAR, receipt_url VARCHAR, notes VARCHAR(255), status VARCHAR(255), created_at TIMESTAMP, CONSTRAINT fk_ride_id3 FOREIGN KEY (ride_id) REFERENCES "ride"."ride"(id) ON DELETE NO ACTION);

CREATE TABLE IF NOT EXISTS "ride"."ride_user_ratings"(id SERIAL PRIMARY KEY, user_id INTEGER, ride_id INTEGER, rating_id INTEGER);