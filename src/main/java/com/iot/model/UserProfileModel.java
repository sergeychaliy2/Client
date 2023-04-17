package com.iot.model;

public class UserProfileModel {

    private final static UserProfileModel instance = new UserProfileModel();
    private String userInstance;

    public static UserProfileModel getInstance() {
        return instance;
    }
    public String getUserInstance() {
        return userInstance;
    }

    public void setUserInstance(String userInstance) {
        this.userInstance = userInstance;
    }
}
