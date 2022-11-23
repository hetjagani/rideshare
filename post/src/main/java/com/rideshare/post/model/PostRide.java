package com.rideshare.post.model;

import com.rideshare.post.webentity.PostImage;
import com.rideshare.post.webentity.Ride;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class PostRide extends Post{
    private Ride ride;

    public PostRide(Ride ride) {
        super();
        this.ride = ride;
    }

    public PostRide(Post post) {
        super(post.getId(), post.getUserId(), post.getTitle(), post.getDescription(), post.getCreatedAt(), post.getUpdatedAt(), post.getType(), post.getRefId(), post.getNoOfLikes(), post.getImageList(), post.getUser());
    }

    public PostRide(Integer userId, String title, String description, Timestamp createdAt, Timestamp updatedAt, String type, Integer rideId, Integer noOfLikes, List<PostImage> imageList, Ride ride, UserInfo user) {
        super(userId, title, description, createdAt, updatedAt, type, rideId, noOfLikes, imageList, user);
        this.ride = ride;
    }

    public PostRide(Integer id, Integer userId, String title, String description, Timestamp createdAt, Timestamp updatedAt, String type, Integer rideId, Integer noOfLikes, List<PostImage> imageList, Ride ride, UserInfo user) {
        super(id, userId, title, description, createdAt, updatedAt, type, rideId, noOfLikes, imageList, user);
        this.ride = ride;
    }
}
