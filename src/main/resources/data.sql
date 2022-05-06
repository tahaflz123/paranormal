INSERT INTO users (id, created_date, username, password, email, role, deleted)
VALUES (1, now(), 'wolf_larsen', '$2a$10$JdMSNoDmdvZ.sB91Cn8QrOuLn86GQHhdAGSTWVbZOc1INzx.KxnTO', 'taha.433@outlook.com', 'USER', false);

INSERT INTO users (id, created_date, username, password, email, role, deleted)
VALUES (5, now(), 'crazy_boy', '$2a$10$JdMSNoDmdvZ.sB91Cn8QrOuLn86GQHhdAGSTWVbZOc1INzx.KxnTO', 'crazy_boy@gmail.com', 'USER', false);

INSERT INTO posts (id, created_date, header, content, user_id, deleted)
VALUES (2, now(), 'the Graveyard', 'content of the Graveyard', 1, false);

INSERT INTO comments (id, created_date, content, post_id, user_id, deleted)
VALUES (3, now(), 'the best paranormal story ever', 2, 5, false);

INSERT INTO likes (id, created_date, post_id, user_id)
VALUES (4, now(), 2, 4);

INSERT INTO users (id, created_date, username, password, email, role, deleted)
VALUES (6, now(), 'admin', '$2a$10$JdMSNoDmdvZ.sB91Cn8QrOuLn86GQHhdAGSTWVbZOc1INzx.KxnTO', 'email@paranormal.com', 'ADMIN', false);

INSERT INTO comments (id, created_date, content, post_id, user_id, deleted)
VALUES (7, now(), 'laskdflkalsdkfasdf', 2, 1, false);

INSERT INTO comments (id, created_date, content, post_id, user_id, deleted)
VALUES (8, now(), 'some comments', 2, 1, false);

INSERT INTO comments (id, created_date, content, post_id, user_id, deleted)
VALUES (9, now(), 'this comment deleted', 2, 1, true);