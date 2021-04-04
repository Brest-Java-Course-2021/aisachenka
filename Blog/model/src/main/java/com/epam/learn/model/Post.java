package com.epam.learn.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class Post {
    private Integer postId;

    private Integer blogId;

    @NotBlank(message = "Post test is mandatory")
    @Size(min = 1, max = 300, message = "Text should be b-n 1 and 300 characters")
    private String text;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please provide a date.")
    private LocalDate localDate;

    @NotNull(message = "number of Likes is mandatory")
    @Min(value = 0, message = "number of Likes should be greater or equal zero")
    @Max(value = Integer.MAX_VALUE, message = "number of Likes should be lesser than 2,147,483,647")
    private Integer numberOfLikes;

    public Post(Integer blogId, String text, LocalDate localDate, Integer numberOfLikes) {
        this.blogId = blogId;
        this.text = text;
        this.localDate = localDate;
        this.numberOfLikes = numberOfLikes;
    }

    public Post() {
    }

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
