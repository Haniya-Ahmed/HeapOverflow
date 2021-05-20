--Table creation

START TRANSACTION;

CREATE TABLE IF NOT EXISTS accounts(
    id SERIAL,
    username varchar(128) NOT NULL UNIQUE,
    password varchar(257) NOT NULL,
    moderator BOOLEAN NOT NULL,
    PRIMARY KEY(id)
);

INSERT INTO accounts VALUES(DEFAULT, 'admin', sha1('Team1Admin'), true);

CREATE TABLE IF NOT EXISTS tags(
    id SERIAL,
    tagName varchar(128) NOT NULL UNIQUE,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS posts(
    id SERIAL,
    userId BIGINT UNSIGNED NOT NULL,
    title VARCHAR(1024) NOT NULL,
    body VARCHAR(8192) NOT NULL,
    date_posted TIMESTAMP NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(userId) REFERENCES accounts(id)
);

CREATE TABLE IF NOT EXISTS posts_tags(
    postId BIGINT UNSIGNED NOT NULL,
    tagId BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY(postId) REFERENCES posts(id) ON DELETE CASCADE,
    FOREIGN KEY(tagId) REFERENCES tags(id)
);

CREATE TABLE IF NOT EXISTS responses(
    id SERIAL,
    userId BIGINT UNSIGNED NOT NULL,
    parent BIGINT UNSIGNED NOT NULL,
    body varchar(8192) NOT NULL,
    score INTEGER DEFAULT 0,
    PRIMARY KEY(id),
    FOREIGN KEY(userId) REFERENCES accounts(id),
    FOREIGN KEY(parent) REFERENCES posts(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS votes(
    id SERIAL,
    userId BIGINT UNSIGNED NOT NULL,
    responseId BIGINT UNSIGNED NOT NULL,
    type TINYINT,
    PRIMARY KEY(id),
    FOREIGN KEY(userId) REFERENCES accounts(id),
    FOREIGN KEY(responseId) REFERENCES responses(id) ON DELETE CASCADE
);

--Add stored procedures

DELIMITER //

CREATE PROCEDURE add_account(
	newUser VARCHAR(128),
    newPass VARCHAR(257)
)
BEGIN
    INSERT INTO accounts VALUES(DEFAULT, newUser, sha1(newPass), false);
        
	IF (ROW_COUNT() = 0) THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'No rows added';
    ELSE
        SELECT TRUE;
	END IF;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE add_tag(
	tagName VARCHAR(128)
)
BEGIN
    INSERT INTO tags VALUES(DEFAULT, tagName);
        
	IF (ROW_COUNT() = 0) THEN
		SIGNAL SQLSTATE '45001'
			SET MESSAGE_TEXT = 'No rows added';
    ELSE
        SELECT TRUE;
	END IF;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE add_post(
	userId BIGINT UNSIGNED,
    title VARCHAR(256),
    body VARCHAR(8192),
    tags VARCHAR(1024)
)
label:BEGIN
    DECLARE loc INT;
    DECLARE nextTag INT;
    DECLARE substr VARCHAR(1024);
    START TRANSACTION;
        INSERT INTO posts VALUES(DEFAULT, userId, title, body, NOW());
        IF (ROW_COUNT() = 0) THEN
            SIGNAL SQLSTATE '45001'
                SET MESSAGE_TEXT = 'No rows added';
        END IF;

        SET loc = LOCATE('<', tags);
        SET substr = tags;

        WHILE loc != 0 DO
            SET substr = SUBSTRING(substr, loc + 1);
            SET nextTag = (SELECT tags.id FROM tags WHERE tagName = SUBSTRING_INDEX(substr, '>', 1) LIMIT 1);
            INSERT INTO posts_tags VALUES(LAST_INSERT_ID(), nextTag);
            SET loc = LOCATE('<', substr);
        END WHILE;

        IF (ROW_COUNT() = 0) THEN
            SIGNAL SQLSTATE '45002'
            SET MESSAGE_TEXT = 'No tags added to post, deleting post';
            LEAVE label;
        END IF;
    COMMIT;
    SELECT posts.id, userId, username, title, body, date_posted, tags.id, tagName 
        FROM posts 
        LEFT JOIN accounts ON userId = accounts.id
        LEFT JOIN posts_tags ON posts.id = posts_tags.postId OR posts_tags.postId = NULL
        LEFT JOIN tags ON posts_tags.tagId = tags.id
        WHERE posts.id = LAST_INSERT_ID();
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE add_response(
	userId BIGINT UNSIGNED,
    parentId BIGINT UNSIGNED,
    body varchar(8192)
)BEGIN
    INSERT INTO responses VALUES(DEFAULT, userId, parentId, body, DEFAULT);
    IF (ROW_COUNT() = 0) THEN
        SIGNAL SQLSTATE '45001'
            SET MESSAGE_TEXT = 'No rows added';
    END IF;

    SELECT responses.id, userId, username, parent, body, score
        FROM responses 
        LEFT JOIN accounts ON userId = accounts.id 
        WHERE responses.id = LAST_INSERT_ID();
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE add_vote(
	userId BIGINT UNSIGNED,
    responseId BIGINT UNSIGNED,
    type TINYINT
)BEGIN
    INSERT INTO votes VALUES(DEFAULT, userId, responseId, type);
    IF (ROW_COUNT() = 0) THEN
        SIGNAL SQLSTATE '45001'
            SET MESSAGE_TEXT = 'No rows added';
    ELSE
        SELECT TRUE;
        UPDATE responses SET score = score + type WHERE id = responseId;
    END IF;
END //
DELIMITER ;

--Get stored procedures
DELIMITER //

CREATE PROCEDURE check_account_exists(
	toCheck VARCHAR(128)
)BEGIN
    IF((SELECT 1 FROM accounts WHERE EXISTS(SELECT * FROM accounts WHERE username = toCheck) LIMIT 1)) THEN
        SELECT TRUE;
    ELSE
        SELECT FALSE;
    END IF;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE user_login(
	user VARCHAR(128),
    pass VARCHAR(257)
)BEGIN
    SELECT * FROM accounts WHERE user = accounts.username AND sha1(pass) = accounts.password;
END //
DELIMITER ;

COMMIT;

DELIMITER //

CREATE PROCEDURE get_tags_by_post(
	searchId BIGINT UNSIGNED
)BEGIN
    SELECT tags.id, tagName FROM posts
        JOIN posts_tags ON posts.id = posts_tags.postId
        JOIN tags ON posts_tags.tagId = tags.id WHERE postId = searchId;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE get_posts_by_date_posted(
	numRows INTEGER
)BEGIN
    SET @limit = numRows;
    PREPARE STMT FROM 'SELECT posts.id, userId, username, title, body, date_posted, tags.id, tagName 
                            FROM posts
                            LEFT JOIN accounts ON userId = accounts.id
                            LEFT JOIN posts_tags ON posts.id = posts_tags.postId OR posts_tags.postId = NULL
                            LEFT JOIN tags ON posts_tags.tagId = tags.id
                            ORDER BY date_posted DESC LIMIT ?';
    EXECUTE STMT USING @limit;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE get_posts_by_id(
	searchId BIGINT UNSIGNED
)BEGIN
    SELECT posts.id, userId, username, title, body, date_posted, tags.id, tagName 
                            FROM posts
                            LEFT JOIN accounts ON userId = accounts.id
                            LEFT JOIN posts_tags ON posts.id = posts_tags.postId OR posts_tags.postId = NULL
                            LEFT JOIN tags ON posts_tags.tagId = tags.id 
                            WHERE posts.id = searchId;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE get_posts_by_tag(
	searchTag VARCHAR(128)
)BEGIN
    DECLARE relatedTag BIGINT UNSIGNED;
    SET relatedTag = (SELECT id FROM tags WHERE searchTag = tagName);
    SELECT posts.id, userId, username, title, body, date_posted, tags.id, tagName 
        FROM posts 
        LEFT JOIN accounts ON userId = accounts.id
        LEFT JOIN posts_tags ON posts.id = posts_tags.postId OR posts_tags.postId = NULL
        LEFT JOIN tags ON posts_tags.tagId = tags.id
        WHERE relatedTag = tagId;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE get_responses_by_parent(
	searchId BIGINT UNSIGNED
)BEGIN
    SELECT responses.id, userId, username, parent, body, score
        FROM responses 
        LEFT JOIN accounts ON userId = accounts.id 
        WHERE parent = searchId;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE get_responses_by_id(
	searchId BIGINT UNSIGNED
)BEGIN
    SELECT responses.id, userId, username, parent, body, score
        FROM responses 
        LEFT JOIN accounts ON userId = accounts.id 
        WHERE responses.id = searchId;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE get_votes_by_user_and_response(
	userSearchId BIGINT UNSIGNED,
    userResponseId BIGINT UNSIGNED
)BEGIN
    SELECT * FROM votes WHERE userId = userSearchId AND responseId = userResponseId;
END //
DELIMITER ;

--Edit stored procedures
DELIMITER //

CREATE PROCEDURE edit_post_body(
	newBody VARCHAR(8192),
    searchId BIGINT UNSIGNED
)BEGIN
    UPDATE posts SET body = newBody WHERE id = searchId;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE edit_response_body(
	newBody VARCHAR(8192),
    searchId BIGINT UNSIGNED
)BEGIN
    UPDATE responses SET responses.body = newBody WHERE id = searchId;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE edit_vote(
	newVote TINYINT,
    searchId BIGINT UNSIGNED
)BEGIN
    DECLARE oldVote TINYINT;
    DECLARE relatedResponse BIGINT UNSIGNED;
    SET relatedResponse = (SELECT responseId FROM votes WHERE votes.id = searchId);
    SET oldVote = (SELECT type FROM votes WHERE votes.id = searchId);
    UPDATE votes SET votes.type = newVote WHERE votes.id = searchId;
    UPDATE responses SET score = score - oldVote + newVote WHERE responses.id = relatedResponse;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE add_tags_by_postId(
    newTags VARCHAR(1024),
    searchId BIGINT UNSIGNED
)
label:BEGIN
    DECLARE loc INT;
    DECLARE nextTag INT;
    DECLARE substr VARCHAR(1024);
    DECLARE relatedPost BIGINT UNSIGNED;
    START TRANSACTION;
        SET relatedPost = (SELECT posts.id FROM posts WHERE posts.id = searchId);
        IF (relatedPost = 0) THEN
            SIGNAL SQLSTATE '45001'
                SET MESSAGE_TEXT = 'Could not find post.';
        END IF;

        SET loc = LOCATE('<', newTags);
        SET substr = newTags;

        WHILE loc != 0 DO
            SET substr = SUBSTRING(substr, loc + 1);
            SET nextTag = (SELECT tags.id FROM tags WHERE tagName = SUBSTRING_INDEX(substr, '>', 1) LIMIT 1);
            INSERT INTO posts_tags VALUES(relatedPost, nextTag);
            SET loc = LOCATE('<', substr);
        END WHILE;

        IF (ROW_COUNT() = 0) THEN
            SIGNAL SQLSTATE '45002'
            SET MESSAGE_TEXT = 'No tags added to post, deleting post';
            LEAVE label;
        ELSE
            SELECT TRUE;
        END IF;
    COMMIT;
END //
DELIMITER ;

--Delete stored procedures

DELIMITER //

CREATE PROCEDURE delete_post(
	searchId BIGINT UNSIGNED
)BEGIN
    DELETE FROM posts_tags WHERE postId = searchId;
    DELETE FROM responses WHERE parent = searchId;
    DELETE FROM posts WHERE id = searchId;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE delete_response(
	searchId BIGINT UNSIGNED
)BEGIN
    DELETE FROM votes WHERE responseId = searchId;
    DELETE FROM responses WHERE id = searchId;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE delete_vote(
	searchId BIGINT UNSIGNED
)BEGIN
    DECLARE oldVote TINYINT;
    DECLARE relatedResponse BIGINT UNSIGNED;
    SET oldVote = (SELECT type FROM votes WHERE id = searchId);
    SET relatedResponse = (SELECT responseId FROM votes WHERE votes.id = searchId);
    UPDATE responses SET score = score - oldVote WHERE responses.id = relatedResponse;
    DELETE FROM votes WHERE id = searchId;
    DELETE FROM responses WHERE id = relatedResponse;
END //
DELIMITER ;

DELIMITER //

CREATE PROCEDURE delete_tag(
    searchId BIGINT UNSIGNED,
    oldTags VARCHAR(1024)
)
label:BEGIN
    DECLARE loc INT;
    DECLARE nextTag INT;
    DECLARE substr VARCHAR(1024);
    DECLARE relatedPost BIGINT UNSIGNED;
    START TRANSACTION;
        SET relatedPost = (SELECT posts.id FROM posts WHERE posts.id = searchId);
        IF (relatedPost = 0) THEN
            SIGNAL SQLSTATE '45001'
                SET MESSAGE_TEXT = 'Could not find post.';
        END IF;

        SET loc = LOCATE('<', oldTags);
        SET substr = oldTags;

        WHILE loc != 0 DO
            SET substr = SUBSTRING(substr, loc + 1);
            SET nextTag = (SELECT tags.id FROM tags WHERE tagName = SUBSTRING_INDEX(substr, '>', 1) LIMIT 1);
            DELETE FROM posts_tags WHERE posts_tags.postId = searchId AND posts_tags.tagId = nextTag;
            SET loc = LOCATE('<', substr);
        END WHILE;

        IF (ROW_COUNT() = 0) THEN
            SIGNAL SQLSTATE '45002'
            SET MESSAGE_TEXT = 'No tags added to post, deleting post';
            LEAVE label;
        ELSE
            SELECT TRUE;
        END IF;
    COMMIT;
END //
DELIMITER ;

COMMIT;
