package com.rideshare.userinfo.webentity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserInfo {
    @NotNull
    private Integer id;
    private String firstName;
    private String lastName;
    private String profileImage;

    public UserInfo() {}

    public UserInfo(Integer id, String firstName, String lastName, String profileImage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
