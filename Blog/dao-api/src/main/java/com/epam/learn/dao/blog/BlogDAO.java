package com.epam.learn.dao.blog;

import com.epam.learn.model.Blog;

import java.util.List;
import java.util.Optional;
public interface BlogDAO {
    // Если не находит класс из другого модуля его нужно установить в локальный репозиторий мавена
    /**
     * this function call datasource and select all blogs and translate it in List
     *
     * @return list of all finded blogs
     * */
    List<Blog> findAll();

    /**
     * this function call datasource and find blog by id
     *
     * @return Optional of nullable i.e. it contains Blog or not
     * */
    Optional<Blog> findById(Integer blogId);


    /**
     * this function call datasource and create blog
     *
     * @return number of id of created Blog
     * */
    Integer create(Blog blog);

    /**
     * this function call datasource and update blog
     *
     * @return number of updated Blogs
     * */
    Integer update(Blog blog);

    /**
     * this function call datasource and delete blog
     *
     * @return number of deleted Blogs
     * */
    Integer delete(Integer blogId);



    /**
     * this function call datasource and find blog by Blog's name
     *
     * @return Optional of nullable i.e. it contains Blog or not
     * */
    Optional<Blog> findByName(String blogName);
}
