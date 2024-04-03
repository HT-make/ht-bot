package com.htmake.htbot.unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import org.springframework.stereotype.Component;

@Component
public interface HttpClient {

    HttpResponse<JsonNode> sendPostRequest(String endPoint, String requestBody);
    HttpResponse<JsonNode> sendGetRequest(String endPoint, Pair<String, String> routeParam);
    HttpResponse<JsonNode> sendPatchRequest(String endPoint, Pair<String, String> routeParam, String requestBody);
}
