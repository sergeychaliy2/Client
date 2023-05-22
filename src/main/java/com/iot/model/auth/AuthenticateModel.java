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
    private boolean isAuthorized = false;
    private boolean isAppFirstOpen = true;
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
                        if (str.length > 2) { throw new RuntimeException("Illegal array size"); }
                        switch (str[0]) {
                            case "refresh" ->  refreshToken = str[1];
                            case "login"   ->  userLogin = str[1];
                            case "password"->  userPassword = str[1];
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isFieldsSimilar(String login, String password, String refresh) {
        return this.userLogin.equals(login) && this.userPassword.equals(password) && this.refreshToken.equals(refresh);

    }

    public void updateFileData(String login, String password, String refreshToken, String accessToken) {
        if (isFieldsSimilar(login, password, refreshToken)) return;

        this.userLogin = login;
        this.userPassword = password;
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile, false))) {
            writer.write(String.format("%s,%s", "login", login));
            writer.write(String.format("\n%s,%s", "password", password));
            writer.write(String.format("\n%s,%s", "refresh", refreshToken));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.isAuthorized = true;
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

    public void setIsAuthorized(boolean state) {
        this.isAuthorized = state;
    }
    public boolean getIsAuthorized() { return isAuthorized; }

    public String getUserLogin() {
        return userLogin;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public boolean isAppFirstOpen () {
        return this.isAppFirstOpen;
    }

    public void setAppFirstOpen (boolean isAppFirstOpen) {
        this.isAppFirstOpen = isAppFirstOpen;
    }
}
