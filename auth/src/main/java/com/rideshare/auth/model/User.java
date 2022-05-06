package com.rideshare.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class User {
    private Integer id;
    private String email;
    @JsonIgnore
    private String password;
    private String phoneNo;
    private Boolean isVerified;
    private List<String> roles;

    public User() {}

    public User(Integer id, String email, String password, String phoneNo, Boolean isVerified, List<String> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.isVerified = isVerified;
        this.roles = roles;
    }

    public User(Integer id, String email, String password, String phoneNo, Boolean isVerified) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.isVerified = isVerified;
        this.roles = roles;
    }

    public User(String email, String password, String phoneNo, Boolean isVerified, List<String> roles) {
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.isVerified = isVerified;
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
