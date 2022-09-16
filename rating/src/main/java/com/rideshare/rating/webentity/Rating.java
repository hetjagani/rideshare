package com.rideshare.rating.webentity;

import lombok.Data;

import java.util.List;

@Data
public class Rating {
    private Integer id;
    private User user;
    private User ratingUser;
    private Float rating;
    private String description;
    private List<String> liked; // Items Liked about the ride/ driver
    private List<String> disliked; // Items Disliked about the ride/ driver

    public Rating(){}

    public Rating(Integer id, User user, User ratingUser, Float rating, String description, List<String> liked, List<String> disliked) {
        this.id = id;
        this.user = user;
        this.ratingUser = ratingUser;
        this.rating = rating;
        this.description = description;
        this.liked = liked;
        this.disliked = disliked;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", user=" + user +
                ", ratingUser=" + ratingUser +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", liked=" + liked +
                ", disliked=" + disliked +
                '}';
    }
}
