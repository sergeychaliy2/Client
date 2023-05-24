package com.iot.model.constants;

public final class Endpoints {

    private Endpoints() {}
    public static final String SEND_CODE = "/code/send";
    public static final String CONFIRM_CODE = "/code/verify";
    public static final String AUTHORIZATION = "/account/login";
    public static final String REGISTRATION = "/account/register";
    public static final String RESET_PASSWORD = "/account/change/password";
    public static final String APP_CONNECTION = "/connection/app";
    public static final String DEVICE_GETTING_UPDATES = "/management/app/updates";
    public static final String UPDATE_TOKEN = "/token/update";
    public static final String ALL_DEVICES = "/management/user/devices";
    public static final String ONE_DEVICE = "/management/user/devices/%d";
    public static final String STATE_CHANGE = "/management/user/devices/%d/change";
//    public static final String RESET_STATUS = "/management/user/devices/{id}/reset";
}