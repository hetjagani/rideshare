package com.rideshare.rating.service;

import com.rideshare.rating.exception.RatingDoesNotExisitException;
import com.rideshare.rating.mapper.TagMapper;
import com.rideshare.rating.model.Rating;
import com.rideshare.rating.webentity.User;
import com.rideshare.rating.webentity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService implements IRatingService{

    private final String createRating = "INSERT INTO \"rating\".\"rating\" (user_id, rating_user_id, rating, description) VALUES (?, ?, ?, ?) RETURNING id";
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

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.userinfo.url}")
    private String userInfoURL;

    public com.rideshare.rating.webentity.Rating getRatingById(Integer id, String token) throws Exception {
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(getDetailedRatingById, id);
            com.rideshare.rating.webentity.Rating rating = new com.rideshare.rating.webentity.Rating();

            List<String> liked = new ArrayList<>();
            List<String> disliked = new ArrayList<>();
            Integer userId = null;
            Integer ratingUserId = null;
            int rowCount = 0;
            while (rs.next()) {
                rating.setId(rs.getInt(1));
                userId = rs.getInt(2);
                ratingUserId = rs.getInt(3);
                rating.setRating(rs.getFloat(4));
                rating.setDescription(rs.getString(5));
                if (rs.getBoolean(9) == true) {
                    liked.add(rs.getString(11));
                } else {
                    disliked.add(rs.getString(11));
                }
                rowCount++;
            }

            if(rowCount == 0){
                throw new RatingDoesNotExisitException("Rating does not exist");
            }

            String requestURL = userInfoURL + "/users/" + userId;
            HttpHeaders header = new HttpHeaders();
            header.add("Authorization", token);

            HttpEntity request = new HttpEntity(header);

            ResponseEntity<UserInfo> userInfo = restTemplate.exchange(requestURL, HttpMethod.GET, request, UserInfo.class);
            User user = null;

            if(userInfo.getBody() != null){
                user = new User(userInfo.getBody().getFirstName(), userInfo.getBody().getLastName(),
                        userInfo.getBody().getProfileImage());;
            }
            rating.setUser(user);

            String requestURLForRatingUser = userInfoURL + "/users/" + ratingUserId;
            ResponseEntity<UserInfo> ratingUserInfo = restTemplate.exchange(requestURLForRatingUser, HttpMethod.GET,
                                                        request, UserInfo.class);

            User ratingUser = null;

            if(ratingUserInfo.getBody() != null){
                ratingUser = new User(ratingUserInfo.getBody().getFirstName(), ratingUserInfo.getBody().getLastName(),
                        ratingUserInfo.getBody().getProfileImage());;
            }
            rating.setRatingUser(ratingUser);
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
    public Rating create(Rating rating, String token) throws Exception {
        try {
            Integer ratingId = jdbcTemplate.queryForObject(createRating, Integer.class, rating.getUserId(), rating.getRatingUserid(), rating.getRating(), rating.getDescription());
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
            com.rideshare.rating.webentity.Rating newRating = getRatingById(ratingId, token);
            return rating;
        }catch(Exception e){
            throw e;
        }
    }
}