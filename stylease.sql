/* stylease.sql */

CREATE TABLE boards (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created TIMESTAMP DEFAULT NOW(),
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    stormpath_username VARCHAR(255) UNIQUE
);

CREATE TABLE keys (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

INSERT INTO keys (name) VALUES
('Public Read'), ('Public Write');

CREATE TABLE board_keys (
    boardid INTEGER,
    keyid INTEGER,
    PRIMARY KEY (boardid, keyid),
    FOREIGN KEY (boardid) REFERENCES boards (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (keyid) REFERENCES keys (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE user_keys (
    userid INTEGER,
    keyid INTEGER,
    PRIMARY KEY (userid, keyid),
    FOREIGN KEY (userid) REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (keyid) REFERENCES keys (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    board INTEGER NOT NULL,
    author INTEGER NOT NULL,
    posted TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (board) REFERENCES boards (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (author) REFERENCES users (id)
        ON UPDATE CASCADE
);

CREATE TABLE styles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE attribs (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE style_attribs (
    id BIGSERIAL PRIMARY KEY,
    attrib INTEGER NOT NULL,
    val TEXT NOT NULL,
    style INTEGER,
    FOREIGN KEY (attrib) REFERENCES attribs (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (style) REFERENCES styles (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE message_styles (
    messageid INTEGER,
    styleid INTEGER,
    PRIMARY KEY (messageid, styleid),
    FOREIGN KEY (messageid) REFERENCES messages (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (styleid) REFERENCES styles (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT INTO styles (name) VALUES
('font-weight'),
('font-style'),
('font-variant'),
('line-height'),
('text-transform'),
('margin'),
('padding'),
('color'),
('vertical-align'),
('text-align'),
('text-decoration'),
('background-color'),
('background-image'),
('overflow'),
('margin'),
('padding')
;
