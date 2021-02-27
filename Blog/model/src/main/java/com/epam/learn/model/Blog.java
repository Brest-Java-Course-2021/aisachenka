package com.epam.learn;

import java.util.Objects;

public class Blog {
    private Integer blogId;
    private String name;

    public Blog(String name){
        this.name = name;
    }

    public Blog(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return blogId.equals(blog.blogId) && name.equals(blog.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blogId, name);
    }
}
