package com.rideshare.post.model;

import com.rideshare.post.webentity.PostImage;
import com.rideshare.post.webentity.Rating;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class PostRating extends Post{
    private Rating rating;

    public PostRating(Rating rating) {
        this.rating = rating;
    }

    public PostRating(Post post){
        super(post.getId(), post.getUserId(), post.getTitle(), post.getDescription(), post.getCreatedAt(), post.getUpdatedAt(), post.getType(), post.getRefId(), post.getNoOfLikes(), post.getImageList(), post.getUser());
    }

    public PostRating(Integer userId, String title, String description, Timestamp createdAt, Timestamp updatedAt, String type, Integer rideId, Integer noOfLikes, List<PostImage> imageList, Rating rating, UserInfo user) {
        super(userId, title, description, createdAt, updatedAt, type, rideId, noOfLikes, imageList, user);
        this.rating = rating;
    }

    public PostRating(Integer id, Integer userId, String title, String description, Timestamp createdAt, Timestamp updatedAt, String type, Integer rideId, Integer noOfLikes, List<PostImage> imageList, Rating rating, UserInfo user) {
        super(id, userId, title, description, createdAt, updatedAt, type, rideId, noOfLikes, imageList, user);
        this.rating = rating;
    }
}
