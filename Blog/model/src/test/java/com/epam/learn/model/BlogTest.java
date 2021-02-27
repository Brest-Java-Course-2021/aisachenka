package com.epam.learn.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class BlogTest {

    @Test
    public void setName() {
        Blog blog = new Blog();
        blog.setBlogName("Кто-то");
        assertEquals(blog.getBlogName(),"Кто-то");
    }

    @Test
    public void getConstructorName(){
        Blog blog = new Blog("Животные");
        assertEquals(blog.getBlogName(),"Животные");
    }

    @Test
    public void setBlogId() {
        Blog blog = new Blog();
        blog.setBlogId(999);
        assertEquals(blog.getBlogId(), Integer.valueOf(999));
    }
}