package com.rideshare.userinfo.model;

public class Place {
    private Integer id;
    private String name;
    private String firstLine;
    private String secondLine;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private UserInfo user;
    private Integer userId;

    public Place() {}

    public Place(Integer id, String name, String firstLine, String secondLine, String city, String state, String country, String zipcode, Integer userId) {
        this.id = id;
        this.name = name;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.userId = userId;
    }

    public Place(String name, String firstLine, String secondLine, String city, String state, String country, String zipcode, Integer userId) {
        this.name = name;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(String firstLine) {
        this.firstLine = firstLine;
    }

    public String getSecondLine() {
        return secondLine;
    }

    public void setSecondLine(String secondLine) {
        this.secondLine = secondLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
