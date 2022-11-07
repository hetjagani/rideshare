INSERT INTO "auth"."user"(email, password, phone_number, is_verified) VALUES('testuser@gmail.com', '$2a$10$BbwFEla6tO9cSiAXhaAePehLdeLJhlWsTMpa.Oti6qe96tKD3W.1q', '6692784685', true);
INSERT INTO "auth"."role"(user_id, role) VALUES(1, 'ADMIN');
INSERT INTO "auth"."role"(user_id, role) VALUES(1, 'DRIVER');
INSERT INTO "auth"."role"(user_id, role) VALUES(1, 'RIDER');

INSERT INTO "auth"."user"(email, password, phone_number, is_verified) VALUES('rider@gmail.com', '$2a$10$BbwFEla6tO9cSiAXhaAePehLdeLJhlWsTMpa.Oti6qe96tKD3W.1q', '6692784685', true);
INSERT INTO "auth"."role"(user_id, role) VALUES(2, 'ADMIN');
INSERT INTO "auth"."role"(user_id, role) VALUES(2, 'RIDER');

INSERT INTO "auth"."user"(email, password, phone_number, is_verified) VALUES('driver@gmail.com', '$2a$10$BbwFEla6tO9cSiAXhaAePehLdeLJhlWsTMpa.Oti6qe96tKD3W.1q', '6692784685', true);
INSERT INTO "auth"."role"(user_id, role) VALUES(3, 'ADMIN');
INSERT INTO "auth"."role"(user_id, role) VALUES(3, 'RIDER');