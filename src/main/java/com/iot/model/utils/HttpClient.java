package com.iot.model.utils;
import com.iot.model.auth.AuthenticateModel;
import javafx.util.Pair;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static com.iot.model.constants.Endpoints.UPDATE_TOKEN;

public final class HttpClient {
    private static final HttpPost post = new HttpPost();
    private static final HttpGet get = new HttpGet();
    private static final HttpPut put = new HttpPut();
    public enum HttpMethods {GET, POST, PUT}
    private HttpClient() {}

    private static Pair<String, String> getAuthHeader(boolean isAccess) {
        AuthenticateModel model = AuthenticateModel.getInstance();
        String token;
        if (isAccess)   token = model.getAccessToken();
        else            token = model.getRefreshToken();

        return new Pair<>("Authorization", String.format("Bearer %s", token));
    }

    public static void execute(JSONObject obj, String endPoint, HttpMethods type) {
        final String path = Configuration.generateURL(true, endPoint);
        Pair<String, String> authPair = getAuthHeader(true);
        HttpRequestBase method;

        switch (type) {
            case POST  -> method = post;
            case PUT   -> method = put;
            case GET   -> method = get;
            default    -> throw new RuntimeException("Method is not exists");
        }

        method.setURI(URI.create(path));

        if (obj == null && method instanceof HttpGet) {
            if (endPoint.equals(UPDATE_TOKEN)) authPair = getAuthHeader(false);

            method.setHeader(authPair.getKey(), authPair.getValue());
            new HttpClientRunner().setOption(method).start();
        } else {
            if (!(method instanceof HttpEntityEnclosingRequestBase castedMethod))
                throw new RuntimeException("Method is not instance of HttpEntityEnclosingRequestBase.class");

            if (obj == null)
                throw new RuntimeException("Body is empty");

            castedMethod.setEntity(new StringEntity(obj.toJSONString(), StandardCharsets.UTF_8));
            castedMethod.setHeader("Accept", "application/json");
            castedMethod.setHeader("Content-type", "application/json");
            castedMethod.setHeader(authPair.getKey(), authPair.getValue());


            new HttpClientRunner().setOption(castedMethod).start();
        }

    }
}
class HttpClientRunner extends Thread {
    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    private HttpUriRequest option;

    public HttpClientRunner setOption(HttpUriRequest option) {
        this.option = option;
        return this;
    }

    @Override
    public void run() {
        if (option == null) throw new RuntimeException("Option is null");

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