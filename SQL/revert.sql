START TRANSACTION;

--Drop delete procedures
DROP PROCEDURE IF EXISTS delete_response;
DROP PROCEDURE IF EXISTS delete_post;
DROP PROCEDURE IF EXISTS delete_vote;
DROP PROCEDURE IF EXISTS delete_tag;

--Drop edit procedures
DROP PROCEDURE IF EXISTS add_tags_by_postId;
DROP PROCEDURE IF EXISTS edit_vote;
DROP PROCEDURE IF EXISTS edit_response_body;
DROP PROCEDURE IF EXISTS edit_post_body;

--Drop get procedures
DROP PROCEDURE IF EXISTS get_votes_by_user_and_response;
DROP PROCEDURE get_responses_by_id;
DROP PROCEDURE get_responses_by_parent;
DROP PROCEDURE IF EXISTS get_posts_by_tag;
DROP PROCEDURE IF EXISTS get_posts_by_id;
DROP PROCEDURE IF EXISTS get_posts_by_date_posted;
DROP PROCEDURE IF EXISTS get_tags_by_post;
DROP PROCEDURE IF EXISTS user_login;
DROP PROCEDURE IF EXISTS check_account_exists;

--Drop add procedures
DROP PROCEDURE IF EXISTS add_vote;
DROP PROCEDURE IF EXISTS add_response;
DROP PROCEDURE IF EXISTS add_post;
DROP PROCEDURE IF EXISTS add_tag;
DROP PROCEDURE IF EXISTS add_account;

--Drop tables

DROP TABLE IF EXISTS votes;
DROP TABLE IF EXISTS responses;
DROP TABLE IF EXISTS posts_tags;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS accounts;

COMMIT;
