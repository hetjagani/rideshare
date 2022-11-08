package com.rideshare.post.webentity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String profileImage;

    public User(){}

    public User(Integer id, String firstName, String lastName, String profileImage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
