package com.rideshare.auth.webentity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class User {
    private String email;
    private String password;
    private String phoneNo;
    private List<String> roles;

    public User() {}

    public User(String email, String password, String phoneNo, List<String> roles) {
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.roles = roles;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
