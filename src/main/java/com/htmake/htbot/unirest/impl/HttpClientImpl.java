package com.htmake.htbot.unirest.impl;

import com.htmake.htbot.discord.util.RestServiceType;
import com.htmake.htbot.interfaces.Pair;
import com.htmake.htbot.unirest.HttpClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class HttpClientImpl implements HttpClient {

    @Override
    public HttpResponse<JsonNode> sendPostRequest(String endPoint, String requestBody) {
        try {
            return Unirest.post(RestServiceType.DEFAULT_URL + endPoint)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP POST request", e);
        }
    }

    @Override
    public HttpResponse<JsonNode> sendGetRequest(String endPoint, Pair<String, String> routeParam) {
        try {
            return Unirest.get(RestServiceType.DEFAULT_URL + endPoint)
                    .routeParam(routeParam.getKey(), routeParam.getValue())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP POST request", e);
        }
    }
}
