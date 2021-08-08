
INSERT INTO member (member_id, created_date, updated_date, email, member_type, name, password, refresh_token, role) VALUES (1, '2021-08-06 12:41:15.025050', '2021-08-06 12:41:15.025050', 'test@gmail.com', null, '닉네임', '{bcrypt}$2a$10$C5Kq5knLtzT7v/9RLOt6weQaAJne/JaGfhDFQJO.XFYAamPmzZAw.', null, 'ROLE_USER');
INSERT INTO member (member_id, created_date, updated_date, email, member_type, name, password, refresh_token, role) VALUES (2, '2021-08-06 12:41:15.025050', '2021-08-06 12:41:15.025050', 'test2@gmail.com', null, '닉네임2', '{bcrypt}$2a$10$C5Kq5knLtzT7v/9RLOt6weQaAJne/JaGfhDFQJO.XFYAamPmzZAw.', null, 'ROLE_USER');
INSERT INTO posts (post_id, created_date, updated_date, content, is_deleted, is_voted, permit_count, product_image_url, product_name, reject_count, title, view_count, vote_deadline, member_id) VALUES (1, '2021-08-06 21:31:56', '2021-08-06 21:32:02', 'test', false, false, 0, null, 'fefe', 1, 'test', 1, '2021-08-06 21:33:24', 1);

INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (1, '2021-08-06 12:41:15.025050', '2021-08-06 12:41:15.025050', 0, 0, 'test1', 1, false, 1, 1);
INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (2, '2021-08-06 13:41:15.025050', '2021-08-06 13:41:15.025050', 0, 0, 'test2', 2, false, 1, 1);
INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (3, '2021-08-06 14:41:15.025050', '2021-08-06 14:41:15.025050', 0, 0, 'test3', 3, false, 2, 1);
INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (4, '2021-08-06 15:41:15.025050', '2021-08-06 15:41:15.025050', 0, 0, 'test4', 4, false, 1, 1);
INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (5, '2021-08-06 16:41:15.025050', '2021-08-06 16:41:15.025050', 1, 1, 'test4', 4, false, 2, 1);
INSERT INTO comment (comment_id, created_date, updated_date, comment_layer, comment_order, content, group_no, is_deleted, member_id, post_id) VALUES (6, '2021-08-06 17:41:15.025050', '2021-08-06 17:41:15.025050', 1, 2, 'test4', 4, false, 1, 1);

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