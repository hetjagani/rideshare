package com.rideshare.rating.model;

import lombok.Data;

import java.util.List;

@Data
public class Rating {
    private Integer id;
    private Integer userId;
    private Integer rating;
    private String description;
    private List<String> liked; // Items Liked about the ride/ driver
    private List<String> disliked; // Items Disliked about the ride/ driver

    public Rating(){}
    public Rating(Integer id, Integer userId, Integer rating, String description, List<String> liked, List<String> disliked) {
        this.id = id;
        this.userId = userId;
        this.rating = rating;
        this.description = description;
        this.liked = liked;
        this.disliked = disliked;
    }

    public Rating(Integer userId, Integer rating, String description, List<String> liked, List<String> disliked) {
        this.userId = userId;
        this.rating = rating;
        this.description = description;
        this.liked = liked;
        this.disliked = disliked;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", userId=" + userId +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", liked=" + liked +
                ", disliked=" + disliked +
                '}';
    }
}
