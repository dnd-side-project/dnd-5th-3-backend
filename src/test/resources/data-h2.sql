
INSERT INTO member (member_id, created_date, updated_date, email, member_type, name, password, refresh_token, role) VALUES (1, '2021-08-06 12:41:15.025050', '2021-08-06 12:41:15.025050', 'test@gmail.com', 'GENERAL', 'moomool', '{bcrypt}$2a$10$C5Kq5knLtzT7v/9RLOt6weQaAJne/JaGfhDFQJO.XFYAamPmzZAw.', null, 'ROLE_USER');
INSERT INTO member (member_id, created_date, updated_date, email, member_type, name, password, refresh_token, role) VALUES (2, '2021-08-06 12:41:15.025050', '2021-08-06 12:41:15.025050', 'test@naver.com', 'GENERAL', 'kang', '{bcrypt}$2a$10$/p94I4bR6CumEXfuUZrJJ.KuAix.eh4pFBKAWkSj.WmMgiFVEKWDy', 'eyJhbGciOiJIUzI1NiJ9.eyJjbGFpbSI6eyJyZWZyZXNoIjoiZmExODk5YmUtNzA3Yi00ZTFhLTlhMTItZjk5M2U0ZmU4OTc5In0sImV4cCI6MTIwOTc2Mjk5NDI4Njh9.-6_9NqpVfcvTQor24x0vJ_KVNWEVzp2wLo2jkytkJcw', 'ROLE_USER');
INSERT INTO member (member_id, created_date, updated_date, email, member_type, name, password, refresh_token, role) VALUES (3, '2021-08-06 12:41:15.025050', '2021-08-06 12:41:15.025050', 'merge@naver.com','GENERAL', 'kim', '{bcrypt}$2a$10$/p94I4bR6CumEXfuUZrJJ.KuAix.eh4pFBKAWkSj.WmMgiFVEKWDy', 'eyJhbGciOiJIUzI1NiJ9.eyJjbGFpbSI6eyJyZWZyZXNoIjoiNDhlYWRhNjUtYzQ0Yi00MmZjLWEyZGQtNWJmOTQ5ODQ2YTkxIn0sImV4cCI6MTIwOTc2Mjk5NDI5NDF9.hYTIWLapqe9rH9pIGy4J3xrv1Dzbvlq-VUzTFUvBCio', 'ROLE_USER');
INSERT INTO posts (post_id, created_date, updated_date, content, is_posts_end, is_voted, permit_count, product_image_url, reject_count, title, vote_deadline, posts_deadline, member_id,rank_count) VALUES (1, '2021-08-06 21:31:56', '2021-08-06 21:32:02', 'test', false, false, 0, 1, 1, 'test', '2021-08-06 21:33:24', '2021-08-13 21:33:24', 1,0);

INSERT INTO vote (vote_id, created_date, updated_date, result, member_id, post_id) VALUES (1,'2021-08-13 09:58:55.000000','2021-08-13 09:58:55.000000','PERMIT',1,1);
INSERT INTO vote (vote_id, created_date, updated_date, result, member_id, post_id) VALUES (2,'2021-08-13 09:58:55.000000','2021-08-13 09:58:55.000000','REJECT',2,1);

INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (1, '2021-08-06 12:41:15.025050', '2021-08-06 12:41:15.025050', 0, 0, 'no.1 test content', 1, false, 1, 1);
INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (2, '2021-08-06 13:41:15.025050', '2021-08-06 13:41:15.025050', 0, 0, 'no.2 test content', 2, false, 2, 1);
INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (3, '2021-08-06 15:41:15.025050', '2021-08-06 15:41:15.025050', 0, 0, 'no.3 test content', 3, false, 3, 1);
INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (4, '2021-08-06 16:41:15.025050', '2021-08-06 16:41:15.025050', 1, 1, 'no.3 reply test content', 3, false, 2, 1);
INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (5, '2021-08-06 17:41:15.025050', '2021-08-06 17:41:15.025050', 1, 1, 'no.3 reply test content', 3, false, 1, 1);

INSERT INTO emoji (emoji_id, detail) VALUES (1, '1');
INSERT INTO emoji (emoji_id, detail) VALUES (2, '2');
INSERT INTO emoji (emoji_id, detail) VALUES (3, '3');
INSERT INTO emoji (emoji_id, detail) VALUES (4, '4');
INSERT INTO emoji (emoji_id, detail) VALUES (5, '5');

INSERT INTO comment_emoji (comment_emoji_id, comment_emoji_count, comment_id, emoji_id) VALUES (1, 1, 1, 1);
INSERT INTO comment_emoji (comment_emoji_id, comment_emoji_count, comment_id, emoji_id) VALUES (2, 1, 1, 2);
INSERT INTO comment_emoji (comment_emoji_id, comment_emoji_count, comment_id, emoji_id) VALUES (3, 1, 1, 3);
INSERT INTO comment_emoji (comment_emoji_id, comment_emoji_count, comment_id, emoji_id) VALUES (4, 1, 2, 2);
INSERT INTO comment_emoji (comment_emoji_id, comment_emoji_count, comment_id, emoji_id) VALUES (5, 1, 3, 5);

INSERT INTO comment_emoji_member (comment_emoji_member_id, comment_emoji_id, member_id) VALUES (1, 1, 1);
INSERT INTO comment_emoji_member (comment_emoji_member_id, comment_emoji_id, member_id) VALUES (2, 1, 2);
INSERT INTO comment_emoji_member (comment_emoji_member_id, comment_emoji_id, member_id) VALUES (3, 2, 2);
INSERT INTO comment_emoji_member (comment_emoji_member_id, comment_emoji_id, member_id) VALUES (4, 2, 1);
INSERT INTO comment_emoji_member (comment_emoji_member_id, comment_emoji_id, member_id) VALUES (5, 5, 2);

INSERT INTO notice (notice_id, title, content, created_date, updated_date) VALUES (1,'no.1 notice title','notice content',sysdate(),sysdate());
INSERT INTO notice (notice_id, title, content, created_date, updated_date) VALUES (2,'no.2 notice title','notice content',sysdate(),sysdate());