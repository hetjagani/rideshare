package com.rideshare.post.webentity;

import lombok.Data;

@Data
public class User {
    private String firstName;
    private String lastName;
    private String profileImage;

    public User(){}

    public User(String firstName, String lastName, String profileImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
