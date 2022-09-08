package com.rideshare.ride.service;

import com.rideshare.ride.mapper.TagsMapper;
import com.rideshare.ride.model.Tag;
import com.rideshare.ride.util.Pagination;
import com.rideshare.ride.webentity.PaginatedEntity;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagsService implements ITagsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String getByIdQuery = "SELECT * FROM \"ride\".\"tags\" WHERE id = ?";
    private final String searchQuery = "SELECT * FROM \"ride\".\"tags\" WHERE name LIKE ? LIMIT ? OFFSET ?;";
    private final String insertQuery = "INSERT INTO \"ride\".\"tags\"(name) VALUES (?) RETURNING id;";
    private final String deleteQuery = "DELETE FROM ride.tags WHERE id = ?";

    public Tag getById(Integer id) throws Exception {
        return jdbcTemplate.queryForObject(getByIdQuery, new TagsMapper(), id);
    }

    @Override
    public PaginatedEntity<Tag> searchTags(String query, Integer page, Integer limit) throws Exception {
        Integer offset = Pagination.getOffset(page, limit);
        query = "%" + query + "%";
        List<Tag> tagList = jdbcTemplate.query(searchQuery, new TagsMapper(), query, limit, offset);

        return new PaginatedEntity<>(tagList, page, limit);
    }

    @Override
    public Tag create(Tag tag) throws Exception {
        Integer id = jdbcTemplate.queryForObject(insertQuery, Integer.class, tag.getName());
        Tag createdTag = getById(id);
        return createdTag;
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        jdbcTemplate.update(deleteQuery, id);
        return true;
    }
}
