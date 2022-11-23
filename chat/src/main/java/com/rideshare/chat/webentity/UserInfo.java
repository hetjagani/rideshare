package com.rideshare.chat.webentity;

import com.rideshare.chat.model.User;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class UserInfo extends User {
    private String firstName;
    private String lastName;
    private String profileImage;
    private Float rating;
    private Integer rides;
    private Integer months;
    private Date createdAt;


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

    public UserInfo(String firstName, String lastName, String profileImage, Date date, Integer months) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.createdAt = date;
        this.months = months;
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
