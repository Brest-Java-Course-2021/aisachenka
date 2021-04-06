package com.epam.learn.model.dto;

import java.time.LocalDate;

public class PostDTO {
    private Integer postId;
    private String blogName;
    private String text;
    private LocalDate localDate;
    private Integer numberOfLikes;

    public PostDTO() {
    }

    public PostDTO(Integer postId, String blogName, String text, LocalDate localDate, Integer numberOfLikes) {
        this.postId = postId;
        this.blogName = blogName;
        this.text = text;
        this.localDate = localDate;
        this.numberOfLikes = numberOfLikes;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
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
