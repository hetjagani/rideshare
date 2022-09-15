package com.rideshare.rating.service;

import com.rideshare.rating.mapper.TagMapper;
import com.rideshare.rating.model.Rating;
import com.rideshare.rating.webentity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService implements IRatingService{

    private final String createRating = "INSERT INTO \"rating\".\"rating\" (user_id, rating, description) VALUES (?, ?, ?) RETURNING id";
    private final String createTag = "INSERT INTO \"rating\".\"tags\" (name) VALUES (?) RETURNING id";
    private final String createRatingTag = "INSERT INTO \"rating\".\"rating_tags\" (rating_id, tag_id, feedback) VALUES (?, ?, ?) Returning id";
    private final String getTagByName = "SELECT id FROM \"rating\".\"tags\" WHERE name=?";
    private final String getAllTags = "SELECT * FROM \"rating\".\"tags\"";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Rating getRatingById(Integer id) throws Exception {
        return null;
    }
    @Override
    public Rating create(Rating rating) throws Exception {
        try {
            Integer ratingId = jdbcTemplate.queryForObject(createRating, Integer.class, rating.getUserId(), rating.getRating(), rating.getDescription());
            List<String> existingTags = jdbcTemplate.query(getAllTags, new TagMapper());

            for (String s : rating.getLiked()) {
                Integer tagId;
                if (!existingTags.contains(s)) {
                    tagId = jdbcTemplate.queryForObject(createTag, Integer.class, s);
                } else {
                    tagId = jdbcTemplate.queryForObject(getTagByName, Integer.class, s);
                }
                Integer ratingTag = jdbcTemplate.queryForObject(createRatingTag, Integer.class, ratingId, tagId, true);
            }

            for (String s : rating.getDisliked()) {
                Integer tagId;
                if (!existingTags.contains(s)) {
                    tagId = jdbcTemplate.queryForObject(createTag, Integer.class, s);
                } else {
                    tagId = jdbcTemplate.queryForObject(getTagByName, Integer.class, s);
                }
                Integer ratingTag = jdbcTemplate.queryForObject(createRatingTag, Integer.class, ratingId, tagId, false);
            }
            Rating newRating = getRatingById(ratingId);
            return rating;
        }catch(Exception e){
            throw e;
        }
    }
}