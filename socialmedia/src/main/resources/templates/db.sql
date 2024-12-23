CREATE TABLE appuser (
    userId BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    bio VARCHAR(200)                                                   
);

CREATE TABLE posts (
    postId BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT,
    content TEXT NOT NULL,
    FOREIGN KEY (userId) REFERENCES appuser(userId) ON DELETE CASCADE
);

CREATE TABLE relationships (
    followerId BIGINT,
    followedId BIGINT,
    PRIMARY KEY (followerId, followedId),
    FOREIGN KEY (followerId) REFERENCES appuser(userId),
    FOREIGN KEY (followedId) REFERENCES appuser(userId)
);

CREATE TABLE postlikes (
    postId BIGINT,
    userId BIGINT,
    PRIMARY KEY (postId, userId),
    FOREIGN KEY (postId) REFERENCES posts(postId),
    FOREIGN KEY (userId) REFERENCES appuser(userId)
);

CREATE TABLE postcomments (
    commentId BIGINT AUTO_INCREMENT PRIMARY KEY,
    postId BIGINT,
    userId BIGINT,
    content VARCHAR(255) NOT NULL,
    FOREIGN KEY (postId) REFERENCES posts(postId),
    FOREIGN KEY (userId) REFERENCES appuser(userId)
);

Further considerations for search:
CREATE INDEX idx_username ON appuser(username);
CREATE INDEX idx_email ON appuser(email);
CREATE INDEX idx_userId ON posts(userId);
CREATE INDEX idx_postId ON postlikes(postId);