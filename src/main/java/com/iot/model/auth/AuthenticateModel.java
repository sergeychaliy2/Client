package com.iot.model.auth;

import com.iot.model.utils.ServerResponse;

import java.io.*;

public class AuthenticateModel {
    private final static AuthenticateModel instance = new AuthenticateModel();
    private final File tempFile;
    private volatile ServerResponse response;
    private boolean isAuthorized = true;
//    private volatile ServerRequest request;
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

//    public void updateToken(AbstractAuthorizationController controller) {
//        HttpClient.getInstance().getWithRefresh(UPDATE_TOKEN.toString());
//        new Thread(() -> {
//            while(true) {
//                if (this.response != null) {
//                    switch (response.responseCode()) {
//                        case HttpStatus.SC_OK -> {
//                            try {
//                                JSONParser parser = new JSONParser();
//                                JSONObject resultObject = (JSONObject) parser.parse(response.responseMsg());
//                                this.accessToken = resultObject.get("accessToken").toString();
//                            } catch (ParseException e) {
//                                throw new RuntimeException(e);
//                            }
//
//                            if (request == null) throw new RuntimeException("Server request data is not defined");
//
//                            HttpClient instance = HttpClient.getInstance();
//
//                            if (request.reqType().equals(HttpRequestTypes.POST)) instance.post(request.postBody(), request.underPath());
//                            else                                                 instance.get(request.underPath());
//                            System.out.println(".");
//
//
//                        }
//                        case HttpStatus.SC_INTERNAL_SERVER_ERROR -> {
//                            throw new RuntimeException("РЕФРЕШ ТОКЕН ПРОСРОЧЕН");
//                            //Выкинуть из профиля
//                        }
//                    }
//                    response = null;
//                    request = null;
//                    controller.getLoadingCircle().setVisible(false);
//                    controller.checkServerResponseIs();
//                    break;
//                }
//            }
//        }).start();
//    }

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
            try {
                BufferedReader reader = new BufferedReader(new FileReader(tempFile));
                refreshToken = reader.readLine();
                reader.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile, false));
            writer.write(refreshToken);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isAuthorized() {
        return isAuthorized;
    }
    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

//    public void setRequest(ServerRequest request) {
//        this.request = request;
//    }

}
