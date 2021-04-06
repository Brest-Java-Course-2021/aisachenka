package com.epam.learn.service.impl.post;

import com.epam.learn.dao.post.PostDAO;
import com.epam.learn.model.Post;
import com.epam.learn.service.post.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    PostDAO postDAO;

    public PostServiceImpl(PostDAO postDAO) {
        this.postDAO = postDAO;
    }

    @Override
    public List<Post> findAll() {
        return postDAO.findAll();
    }

    @Override
    public Optional<Post> findById(Integer id) {
        return postDAO.findById(id);
    }

    @Override
    public Integer create(Post post) {
        return postDAO.create(post);
    }

    @Override
    public Integer update(Post post) {
        return postDAO.update(post);
    }

    @Override
    public Integer delete(Integer id) {
        return postDAO.delete(id);
    }

    @Override
    public List<Post> searchByTwoDates(LocalDate dateBefore, LocalDate dateAfter) {
        return postDAO.searchByTwoDates(dateBefore,dateAfter);
    }
}
