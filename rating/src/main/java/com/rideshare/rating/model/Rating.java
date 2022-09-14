package com.rideshare.rating.model;

import lombok.Data;

import java.util.List;

@Data
public class Rating {
    private Integer id;
    private Integer rating;
    private String description;
    private List<String> liked; // Items Liked about the ride/ driver
    private List<String> disliked; // Items Disliked about the ride/ driver

    public Rating(Integer id, Integer rating, String description, List<String> liked, List<String> disliked) {
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.liked = liked;
        this.disliked = disliked;
    }

    public Rating(Integer rating, String description, List<String> liked, List<String> disliked) {
        this.rating = rating;
        this.description = description;
        this.liked = liked;
        this.disliked = disliked;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "id=" + id +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", liked=" + liked +
                ", disliked=" + disliked +
                '}';
    }
}
