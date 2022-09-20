package com.rideshare.rating.webentity;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {

    private Integer id;
    private String email;

    private String phoneNo;

    private List<String> roles;

    private String firstName;

    private String lastName;

    private String profileImage;

    private Boolean verified;

    public UserInfo(){}

    public UserInfo(Integer id, String email, String phoneNo, List<String> roles, String firstName, String lastName, String profileImage, Boolean verified) {
        this.id = id;
        this.email = email;
        this.phoneNo = phoneNo;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", roles=" + roles +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", verified=" + verified +
                '}';
    }
}
