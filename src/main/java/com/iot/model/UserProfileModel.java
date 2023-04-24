package com.iot.model;

public class UserProfileModel {

    private final static UserProfileModel instance = new UserProfileModel();
    private String userLogin;

    public static UserProfileModel getInstance() {
        return instance;
    }
    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
