package com.iot.model.auth;

import com.iot.model.utils.ServerResponse;

import java.io.*;

public class AuthenticateModel {
    private final static AuthenticateModel instance = new AuthenticateModel();
    private final File tempFile;
    private volatile ServerResponse response;
    private String userLogin;
    private String accessToken;
    private String refreshToken;
    private AuthenticateModel() {
        tempFile = new File("temp.txt");

        try {
            if (!tempFile.exists()) {
                tempFile.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        if (refreshToken == null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
                refreshToken = reader.readLine();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile, false))){
            writer.write(refreshToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
