package com.rideshare.userinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class UserInfo extends User {
    private String firstName;
    private String lastName;
    private String profileImage;

    public UserInfo() {}

    public UserInfo(Integer id, String email, String password, String phoneNo, Boolean isVerified, List<String> roles, String firstName, String lastName, String profileImage) {
        super(id, email, password, phoneNo, isVerified, roles);
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }

    public UserInfo(Integer id, String email, String password, String phoneNo, Boolean isVerified, List<String> roles) {
        super(id, email, password, phoneNo, isVerified, roles);
    }

    public UserInfo(String firstName, String lastName, String profileImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
