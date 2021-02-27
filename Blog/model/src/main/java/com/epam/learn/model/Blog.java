package com.epam.learn.model;

import java.util.Objects;

public class Blog {
    private Integer blogId;
    private String blogName;

    public Blog(String blogName){
        this.blogName = blogName;
    }

    public Blog(){}

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blogId=" + blogId +
                ", name='" + blogName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return blogId.equals(blog.blogId) && blogName.equals(blog.blogName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blogId, blogName);
    }
}
