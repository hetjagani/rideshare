CREATE TABLE
IF NOT EXISTS "post"."post"
    (id INTEGER PRIMARY KEY,
    user_id INTEGER,
    title VARCHAR(255),
    description VARCHAR(1024),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    type VARCHAR(255),
    ride_id INTEGER,
    no_of_likes INTEGER);

CREATE TABLE
IF NOT EXISTS "post"."image"
    (id INTEGER,
    post_id Integer,
    url VARCHAR(1024),
    CONSTRAINT fk_post_id FOREIGN KEY(post_id) REFERENCES "post"."post"(id) ON DELETE CASCADE);

CREATE TABLE
IF NOT EXISTS "post"."reported_post"
    (id INTEGER,
    post_id INTEGER,
    user_id INTEGER,
    reason VARCHAR(1024),
    description VARCHAR(1024),
    CONSTRAINT fk_post_id FOREIGN KEY(post_id) REFERENCES "post"."post"(id) ON DELETE CASCADE);