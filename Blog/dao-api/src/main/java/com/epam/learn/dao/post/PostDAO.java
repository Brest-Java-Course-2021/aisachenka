package com.epam.learn.dao.post;

import com.epam.learn.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostDAO {
    /**
     * Method that find all posts from database and returns it as List
     *
     * @return List of all posts
     * */
    List<Post> findAll();


    /**
     * Method that find post from database by id
     *
     * @param id id that needed to be found
     * @return optional of nullable of Post
     * */
    Optional<Post> findById(Integer id);

    /**
     * Method that add post in database
     *
     * @param post post that needed to be created
     * @return id of created post
     * */
    Integer create(Post post);

    /**
     * Method that update post in database
     *
     * @param post post that needed to be updated with updated values
     * @return number of updated posts
     * */
    Integer update(Post post);

    /**
     * Method that delete post in database
     *
     * @param id of post that needed to be deleted
     * @return number of deleted posts
     * */
    Integer delete(Integer id);

}
