package com.epam.learn.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

public class Post {
    private Integer postId;

    @NotNull(message = "Blog id should be initialized")
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postId, post.postId) && Objects.equals(blogId, post.blogId) && Objects.equals(text, post.text) && Objects.equals(localDate, post.localDate) && Objects.equals(numberOfLikes, post.numberOfLikes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, blogId, text, localDate, numberOfLikes);
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", blogId=" + blogId +
                ", text='" + text + '\'' +
                ", localDate=" + localDate +
                ", numberOfLikes=" + numberOfLikes +
                '}';
    }
}
