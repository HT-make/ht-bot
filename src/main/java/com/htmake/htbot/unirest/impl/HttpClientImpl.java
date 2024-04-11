package com.htmake.htbot.unirest.impl;

import com.htmake.htbot.discord.util.RestServiceType;
import com.htmake.htbot.unirest.HttpClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import kotlin.Pair;

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
    public HttpResponse<JsonNode> sendPostRequest(String endPoint, Pair<String, String> routeParam, String requestBody) {
        try {
            return Unirest.post(RestServiceType.DEFAULT_URL + endPoint)
                    .header("Content-Type", "application/json")
                    .routeParam(routeParam.getFirst(), routeParam.getSecond())
                    .body(requestBody)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP POST request", e);
        }
    }

    @Override
    public HttpResponse<JsonNode> sendGetRequest(String endPoint) {
        try {
            return Unirest.get(RestServiceType.DEFAULT_URL + endPoint)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP GET request", e);
        }
    }

    @Override
    public HttpResponse<JsonNode> sendGetRequest(String endPoint, Pair<String, String> routeParam) {
        try {
            return Unirest.get(RestServiceType.DEFAULT_URL + endPoint)
                    .routeParam(routeParam.getFirst(), routeParam.getSecond())
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP GET request", e);
        }
    }

    @Override
    public HttpResponse<JsonNode> sendPatchRequest(String endPoint, Pair<String, String> routeParam, String requestBody) {
        try {
            return Unirest.patch(RestServiceType.DEFAULT_URL + endPoint)
                    .header("Content-Type", "application/json")
                    .routeParam(routeParam.getFirst(), routeParam.getSecond())
                    .body(requestBody)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTTP PATCH request", e);
        }
    }
}
