package com.rideshare.userinfo.model;

import java.util.List;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
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
