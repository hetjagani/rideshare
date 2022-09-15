CREATE TABLE
IF NOT EXISTS "rating"."rating"
        (id SERIAL PRIMARY KEY,
        user_id Integer,
        rating Integer,
        description VARCHAR(1024));

CREATE TABLE
IF NOT EXISTS "rating"."tags"
        (id SERIAL PRIMARY KEY,
        name VARCHAR(255));

CREATE TABLE
IF NOT EXISTS "rating"."rating_tags"
        (id SERIAL PRIMARY KEY,
        rating_id Integer,
        tag_id Integer,
        feedback BOOLEAN,
        CONSTRAINT fk_tag_id
                FOREIGN KEY(tag_id)
                REFERENCES "rating"."tags"(id)
                ON DELETE CASCADE,
        CONSTRAINT fk_rating_id
                FOREIGN KEY(rating_id)
                REFERENCES "rating"."rating"(id)
                ON DELETE CASCADE);