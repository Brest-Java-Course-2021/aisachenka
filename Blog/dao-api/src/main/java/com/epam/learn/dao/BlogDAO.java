package com.epam.learn.dao;

import com.epam.learn.model.Blog;

import java.util.List;
import java.util.Optional;
public interface BlogDAO {
    // Если не находит класс из другого модуля его нужно установить в локальный репозиторий мавена
    List<Blog> findAll();

    Optional<Blog> findById(Integer blogId);

    Integer create(Blog blog);

    Integer update(Blog blog);

    Integer delete(Integer blogId);
}
