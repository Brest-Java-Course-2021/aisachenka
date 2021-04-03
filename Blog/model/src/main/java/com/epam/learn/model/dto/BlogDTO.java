package com.epam.learn.model.dto;

public class BlogDTO {
    private Integer blogId;
    private String blogName;
    private Integer maxNumberOfLikes;


    public BlogDTO(Integer blogId, String blogName, Integer maxNumberOfLikes) {
        this.blogId = blogId;
        this.blogName = blogName;
        this.maxNumberOfLikes = maxNumberOfLikes;
    }

    public BlogDTO() {}

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public Integer getMaxNumberOfLikes() {
        return maxNumberOfLikes;
    }

    public void setMaxNumberOfLikes(Integer maxNumberOfLikes) {
        this.maxNumberOfLikes = maxNumberOfLikes;
    }
}
