package com.iot.model.utils;
import com.iot.model.auth.AuthenticateModel;
import com.iot.model.constants.Endpoints;
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

    private Pair<String, String> getAuthHeader(boolean isAccess) {
        AuthenticateModel model = AuthenticateModel.getInstance();
        String token;
        if (isAccess)   token = model.getAccessToken();
        else            token = model.getRefreshToken();

        return new Pair<>("Authorization", String.format("Bearer %s", token));
    }

    public void post(JSONObject obj, String endPoint) {
        try {
            final String path = Configuration.getInstance().generate(true, endPoint);
            HttpPost post = new HttpPost(path);
            final StringEntity entity;
            entity = new StringEntity(obj.toString());
            post.setEntity(entity);
            Pair<String, String> authPair = getAuthHeader(true);
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
            Pair<String, String> authPair = getAuthHeader(true);
            get.setHeader(authPair.getKey(), authPair.getValue());
            new HttpClientRunner(get).start();
        } catch (IllegalThreadStateException e) {
            throw new RuntimeException(e);
        }
    }
    public void getWithRefresh() {
        try {
            final String patch = Configuration.getInstance().generate(true, Endpoints.UPDATE_TOKEN);
            HttpGet get = new HttpGet(patch);
            Pair<String, String> authPair = getAuthHeader(false);
            get.setHeader(authPair.getKey(), authPair.getValue());
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
            System.out.println(responseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}