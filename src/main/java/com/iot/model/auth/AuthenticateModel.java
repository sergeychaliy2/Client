package com.iot.model.auth;

import com.iot.model.utils.ServerResponse;

import java.io.*;

public class AuthenticateModel {
    private final static AuthenticateModel instance = new AuthenticateModel();
    private final File dataFile;
    private volatile ServerResponse response;
    private String userLogin;
    private String userPassword;
    private String accessToken;
    private String refreshToken;
    private AuthenticateModel() {
        dataFile = new File("temp.txt");

        try {
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        preInitialFileData();
    }

    private void preInitialFileData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            reader.lines().forEach(line->
                    {
                           String[] str = line.split(",");
                        System.out.println(str[0]);
                        System.out.println(str[1].length());
                           if (str.length > 2) { throw new RuntimeException("Illegal array size"); }
                           switch (str[0]) {
                               case "refresh":      refreshToken = str[1];
                               case "login":        userLogin = str[1];
                               case "password":     userPassword = str[1];
                           }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateFileData() {
        //todo setting new login \ password \ tokens
    }

    public static AuthenticateModel getInstance() {
        return instance;
    }

    public ServerResponse getResponse() {
        return response;
    }

    public void setResponse(ServerResponse response) {
        this.response = response;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile, false))){
//            writer.write(refreshToken);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        //todo
    }
    public boolean isAuthorized() {
        return userLogin != null;
    }

//    public void setRequest(ServerRequest request) {
//        this.request = request;
//    }


    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
