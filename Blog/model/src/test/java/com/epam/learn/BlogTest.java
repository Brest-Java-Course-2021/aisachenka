package com.epam.learn;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlogTest {

    @Test
    public void setName() {
        Blog blog = new Blog();
        blog.setName("Кто-то");
        assertEquals(blog.getName(),"Кто-то");
    }

    @Test
    public void getConstructorName(){
        Blog blog = new Blog("Животные");
        assertEquals(blog.getName(),"Животные");
    }

    @Test
    public void setBlogId() {
        Blog blog = new Blog();
        blog.setBlogId(999);
        assertEquals(blog.getBlogId(), Integer.valueOf(999));
    }
}