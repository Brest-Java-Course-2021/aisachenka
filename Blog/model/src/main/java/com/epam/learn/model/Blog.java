package com.epam.learn.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Blog {

    private Integer blogId;

    @NotBlank(message = "name is mandatory")
    @Size(min = 2, message = "Blog name should be b-n 2 and 50 characters",max = 50)
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
