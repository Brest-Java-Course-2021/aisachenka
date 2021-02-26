package com.epam.learn;

import java.time.LocalDate;

public class Post {
    private Integer postId;
    private Integer blogId;
    private String text;
    private LocalDate localDate;
    private Integer numberOfLikes;

    public Post(Integer blogId, String text, LocalDate localDate, Integer numberOfLikes) {
        this.blogId = blogId;
        this.text = text;
        this.localDate = localDate;
        this.numberOfLikes = numberOfLikes;
    }

    public Post(){}

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Integer getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(Integer numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

}
