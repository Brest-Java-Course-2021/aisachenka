DROP TABLE IF EXISTS BLOG;
CREATE table BLOG
(
    BLOG_ID   INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    BLOG_NAME VARCHAR(50) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS POST;
CREATE table POST
(
    POST_ID         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    BLOG_NAME       VARCHAR(50)  NOT NULL,
    TEXT            VARCHAR(300) NOT NULL,
    NUMBER_OF_LIKES INT          NOT NULL,
    LOCAL_DATE      DATE,
    CONSTRAINT BLOG_POST_FK FOREIGN KEY (BLOG_NAME) REFERENCES BLOG (BLOG_NAME) ON UPDATE CASCADE
);