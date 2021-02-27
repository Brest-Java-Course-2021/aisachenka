package com.epam.learn.model;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PostTest {

    @Test
    public void setPostId() {
        Post post = new Post();
        post.setPostId(12);
        assertEquals(post.getPostId(),Integer.valueOf(12));
    }

    @Test
    public void setBlogId() {
        Post post = new Post();
        post.setBlogId(6);
        assertEquals(post.getBlogId(),Integer.valueOf(6));
    }

    @Test
    public void setText() {
        Post post = new Post();
        post.setText("Blah blah blah");
        assertEquals(post.getText(),"Blah blah blah");
    }

    @Test
    public void setLocalDate() {
        Post post = new Post();
        post.setLocalDate(LocalDate.of(2021,2,26));
        assertEquals(post.getLocalDate(), LocalDate.of(2021,2,26));
    }

    @Test
    public void setNumberOfLikes() {
        Post post = new Post();
        post.setNumberOfLikes(666);
        assertEquals(post.getNumberOfLikes(),Integer.valueOf(666));
    }

    @Test
    public void constructorSettingTest(){
        Post post = new Post(12, "blah", LocalDate.of(2020,12,31),345);
        assertEquals(post.getBlogId(),Integer.valueOf(12));
        assertEquals(post.getText(),"blah");
        assertEquals(post.getLocalDate(),LocalDate.of(2020,12,31));
        assertEquals(post.getNumberOfLikes(),Integer.valueOf(345));
    }
}