INSERT INTO users (id, created_date, username, password, email)
VALUES (1, now(), 'wolf_larsen', '$2a$10$JdMSNoDmdvZ.sB91Cn8QrOuLn86GQHhdAGSTWVbZOc1INzx.KxnTO', 'taha.433@outlook.com');

INSERT INTO users (id, created_date, username, password, email)
VALUES (4, now(), 'crazy_boy', 'pass', 'crazy_boy@gmail.com');

INSERT INTO posts (id, created_date, header, content, user_id)
VALUES (2, now(), 'the Graveyard', 'content of the Graveyard', 1);

INSERT INTO comments (id, created_date, content, post_id, user_id)
VALUES (3, now(), 'the best paranormal story ever', 2, 4);

INSERT INTO likes (id, created_date, post_id, user_id)
VALUES (5, now(), 2, 4);