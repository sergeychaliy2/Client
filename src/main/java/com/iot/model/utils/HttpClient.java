package com.iot.model.utils;
import com.iot.model.auth.AuthenticateModel;
import javafx.util.Pair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class HttpClient {
    private static final HttpClient instance = new HttpClient();
    private HttpClient() {}
    public static HttpClient getInstance() {
        return instance;
    }

    private Pair<String, String> getAuthorizationHeader() {
        AuthenticateModel model = AuthenticateModel.getInstance();
        return new Pair<>("Authorization", String.format("Bearer %s", model.getAccessToken()));
    }

    public void post(JSONObject obj, String endPoint) {
        try {
            final String path = Configuration.getInstance().generate(true, endPoint);
            HttpPost post = new HttpPost(path);
            final StringEntity entity;
            entity = new StringEntity(obj.toString());
            post.setEntity(entity);
            Pair<String, String> authPair = getAuthorizationHeader();
            post.setHeader(authPair.getKey(), authPair.getValue());
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            new HttpClientRunner(post).start();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public void get(String endPoint) {
        try {
            final String patch = Configuration.getInstance().generate(true, endPoint);
            HttpGet get = new HttpGet(patch);
            Pair<String, String> authPair = getAuthorizationHeader();
            get.setHeader(authPair.getKey(), authPair.getValue());
            new HttpClientRunner(get).start();
        } catch (IllegalThreadStateException e) {
            throw new RuntimeException(e);
        }
    }
    public void getWithRefresh(String endPoint) {
        try {
            final String patch = Configuration.getInstance().generate(true, endPoint);
            HttpGet get = new HttpGet(patch);
            get.setHeader("Authorization", String.format("Bearer %s",  AuthenticateModel.getInstance().getRefreshToken()));
            new HttpClientRunner(get).start();
        } catch (IllegalThreadStateException e) {
            throw new RuntimeException(e);
        }
    }
}
class HttpClientRunner extends Thread {
    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    private final HttpUriRequest option;

    public HttpClientRunner(HttpUriRequest option) {
        this.option = option;
    }

    @Override
    public void run() {
        try (CloseableHttpResponse response = httpClient.execute(option)) {
            ServerResponse responseData = new ServerResponse(
                    response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8)
            );
            AuthenticateModel.getInstance().setResponse(responseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}