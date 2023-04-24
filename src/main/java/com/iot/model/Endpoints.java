package com.iot.model;

public enum Endpoints {
    SEND_CODE{
        @Override public String toString(){return "/code/send";}
    },
    CONFIRM_CODE{
        @Override public String toString(){return "/code/verify";}
    },
    AUTHORIZATION{
        @Override public String toString(){return "/account/login";}
    },
    REGISTRATION{
        @Override public String toString(){return "/account/register";}
    },
    RESET_PASSWORD{
        @Override public String toString(){return "/account/change/password";}
    },
    APP_CONNECTION{
        @Override public String toString() { return "/connection/app"; }
    },
    UPDATE_TOKEN {
        @Override public String toString() { return "/token/update"; }
    }
    ,
    RECEIVING_DEVICES {
        @Override public String toString() { return "/management/user/devices"; }
    },
    OBTAINING_DEVICE_INFORMATION {
        @Override public String toString() { return "/management/user/devices/{id}"; }
    },
    STATE_CHANGE {
        @Override public String toString() { return "/management/user/devices/{id}/change"; }
    },
    RESET_STATUS {
        @Override public String toString() { return "/management/user/devices/{id}/reset"; }
    }

}
