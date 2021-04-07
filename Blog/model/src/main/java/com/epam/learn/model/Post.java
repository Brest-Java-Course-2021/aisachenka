package com.epam.learn.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

public class Post {
    private Integer postId;

    @NotBlank(message = "Blog name is mandatory")
    @Size(min = 2, message = "Blog name should be b-n 2 and 50 characters",max = 50)
    private String blogName;

    @NotBlank(message = "Post text is mandatory")
    @Size(max = 300, message = "Text should be b-n 1 and 300 characters")
    private String text;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Date can not be future")
    @NotNull(message = "Please provide a date")
    private LocalDate localDate;

    @NotNull(message = "number of Likes is mandatory")
    @Min(value = 0, message = "number of Likes should be greater or equal zero")
    private Integer numberOfLikes;

    public Post() {
    }

    public Post(@NotBlank(message = "Blog name is mandatory")
                @Size(min = 2, message = "Blog name should be b-n 2 and 50 characters", max = 50)
                        String blogName,
                @NotBlank(message = "Post text is mandatory")
                @Size(max = 300, message = "Text should be b-n 1 and 300 characters")
                        String text,
                @PastOrPresent(message = "Date can not be future")
                @NotNull(message = "Please provide a date.")
                        LocalDate localDate,
                @NotNull(message = "number of Likes is mandatory")
                @Min(value = 0, message = "number of Likes should be greater or equal zero")
                        Integer numberOfLikes) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(postId, post.postId) && Objects.equals(blogName, post.blogName) && Objects.equals(text, post.text) && Objects.equals(localDate, post.localDate) && Objects.equals(numberOfLikes, post.numberOfLikes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, blogName, text, localDate, numberOfLikes);
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", blogName='" + blogName + '\'' +
                ", text='" + text + '\'' +
                ", localDate=" + localDate +
                ", numberOfLikes=" + numberOfLikes +
                '}';
    }
}
