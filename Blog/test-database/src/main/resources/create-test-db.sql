CREATE table blog(
    blogId int not null AUTO_INCREMENT,
    blogName varchar(50) not null unique,
    primary key(blogId)
)