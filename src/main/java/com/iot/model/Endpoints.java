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
    UPDATE_TOKEN {
        @Override public String toString() { return "/token/update"; }
    }

}
