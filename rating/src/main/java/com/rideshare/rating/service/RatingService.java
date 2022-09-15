package com.rideshare.rating.service;

import com.rideshare.rating.exception.RatingDoesNotExisitException;
import com.rideshare.rating.mapper.TagMapper;
import com.rideshare.rating.model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService implements IRatingService{

    private final String createRating = "INSERT INTO \"rating\".\"rating\" (user_id, rating, description) VALUES (?, ?, ?) RETURNING id";
    private final String createTag = "INSERT INTO \"rating\".\"tags\" (name) VALUES (?) RETURNING id";
    private final String createRatingTag = "INSERT INTO \"rating\".\"rating_tags\" (rating_id, tag_id, feedback) VALUES (?, ?, ?) Returning id";
    private final String getTagByName = "SELECT id FROM \"rating\".\"tags\" WHERE name=?";
    private final String getAllTags = "SELECT * FROM \"rating\".\"tags\"";

    private final String getDetailedRatingById = "SELECT *\n" +
            "FROM \"rating\".\"rating\" as A,\n" +
            "\"rating\".\"rating_tags\" as B,\n" +
            "\"rating\".\"tags\" as C\n" +
            "WHERE A.id = B.rating_id\n" +
            "AND B.tag_id = C.id\n" +
            "AND A.id = ?;";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Rating getRatingById(Integer id) throws Exception {
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(getDetailedRatingById, id);
            Rating rating = new Rating();

            List<String> liked = new ArrayList<>();
            List<String> disliked = new ArrayList<>();
            int rowCount = 0;
            while (rs.next()) {
                rating.setId(rs.getInt(1));
                rating.setUserId(rs.getInt(2));
                rating.setRating(rs.getFloat(3));
                rating.setDescription(rs.getString(4));
                if (rs.getBoolean(8) == true) {
                    liked.add(rs.getString(10));
                } else {
                    disliked.add(rs.getString(10));
                }
                rowCount++;
            }

            if(rowCount == 0){
                throw new RatingDoesNotExisitException("Rating does not exist");
            }
            rating.setLiked(liked);
            rating.setDisliked(disliked);
            return rating;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Rating getById(Integer ratingId) throws Exception {
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