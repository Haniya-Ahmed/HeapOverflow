--create rows for each table by using call add procedures
START TRANSACTION;
CALL add_account('Soam', 'password');
CALL add_tag('C');
CALL add_tag('Java');
CALL add_tag('SQL');
CALL add_tag('Postgres');
CALL add_tag('MySQL');
CALL add_post(2, 'First post.', 'How do I start to code my science-based dragon MMO?', '<C><SQL><Java>');
CALL add_post(2, 'Second post.', 'What is Java?', '<Java>');
CALL add_response(1, 1, 'Just no.');
CALL add_vote(2, 1, -1);
CALL check_account_exists("Soam");
COMMIT;
