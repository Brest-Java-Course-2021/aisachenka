package com.epam.learn.dao.jdbc.post;

import com.epam.learn.dao.post.PostDtoDAO;
import com.epam.learn.model.dto.PostDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class PostDtoDAOJdbc implements PostDtoDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostDtoDAOJdbc.class);

    @Value("${postDto.findAllWithBlogName}")
    private String findAllWithBlogsName;

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    RowMapper<PostDTO> rowMapper = BeanPropertyRowMapper.newInstance(PostDTO.class);

    public PostDtoDAOJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<PostDTO> findAllWithBlogName() {
        LOGGER.debug("findAllWithBlogName()");
        return namedParameterJdbcTemplate.query(findAllWithBlogsName,rowMapper);
    }
}
