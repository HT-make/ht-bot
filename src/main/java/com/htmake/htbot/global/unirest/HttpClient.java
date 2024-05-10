package com.htmake.htbot.global.unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HttpClient {

    HttpResponse<JsonNode> sendPostRequest(String endPoint, String requestBody);

    HttpResponse<JsonNode> sendPostRequest(String endPoint, Pair<String, String> routeParam, String requestBody);

    HttpResponse<JsonNode> sendGetRequest(String endPoint);

    HttpResponse<JsonNode> sendGetRequest(String endPoint, Pair<String, String> routeParam);

    HttpResponse<JsonNode> sendGetRequest(String endPoint, Pair<String, String> firstRouteParam, Pair<String, String> secondRouteParam);

    HttpResponse<JsonNode> sendGetRequest(String endPoint, List<Pair<String, String>> requestParamList);

    HttpResponse<JsonNode> sendPatchRequest(String endPoint, Pair<String, String> routeParam, String requestBody);

    HttpResponse<JsonNode> sendPatchRequest(String endPoint, Pair<String, String> firstRouteParam, Pair<String, String> secondRouteParam);
}
