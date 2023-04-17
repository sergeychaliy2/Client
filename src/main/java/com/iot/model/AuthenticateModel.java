package com.iot.model;

import java.io.*;

public class AuthenticateModel {
    private final static AuthenticateModel instance = new AuthenticateModel();
    private final static String temp = "temp.txt";
    private volatile ServerResponse response;
//    private volatile ServerRequest request;
    private String accessToken;
    private String refreshToken;
    public AuthenticateModel() {}

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
                BufferedReader reader = new BufferedReader(new FileReader(getTempFile()));
                refreshToken = reader.readLine();
                reader.close();
            } catch (FileNotFoundException e) {
                //toDO
            } catch (IOException e) {
                //todo
            }
        }
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getTempFile(), false));
            writer.write(refreshToken);
            writer.close();
        } catch (IOException e) {
            //TODO
        }
    }

    private File getTempFile() {
        File file = new File(temp);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                //todo
            }
        }
        return file;
    }

//    public void setRequest(ServerRequest request) {
//        this.request = request;
//    }
}
