package com.htmake.htbot.discord.commands.player.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerJoinButtonEvent {

    private final HttpClient httpClient;
    private final ObjectMapperUtil objectMapperUtil;
    private final ErrorUtil errorUtil;

    public PlayerJoinButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.objectMapperUtil = new ObjectMapperUtil();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(ButtonInteractionEvent event, String job) {
        HttpResponse<JsonNode> firstResponse = firstRequest(event.getUser().getId(), job);
        HttpResponse<JsonNode> secondResponse = secondRequest(event.getUser().getId());

        if (firstResponse.getStatus() == 200 && secondResponse.getStatus() == 200) {
            requestSuccess(event);
        } else {
            errorUtil.sendError(event.getHook(), "게임 가입", "게임 가입을 이용할 수 없습니다. 잠시 후 다시 이용해주세요.");
        }
    }

    private HttpResponse<JsonNode> firstRequest(String playerId, String job) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("playerId", playerId);
        requestData.put("job", job);

        String endPoint = "/player/join";
        String jsonBody = objectMapperUtil.mapper(requestData);
        return httpClient.sendPostRequest(endPoint, jsonBody);
    }

    private HttpResponse<JsonNode> secondRequest(String playerId) {
        String endPoint = "/player/initial/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendPostRequest(endPoint, routeParam);
    }

    private void requestSuccess(ButtonInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":video_game: 게임 가입")
                .setDescription("게임 가입에 성공했습니다!")
                .build();

        event.getHook().editOriginalComponents(Collections.emptyList()).queue();
        event.getHook().editOriginalEmbeds(embed).queue();
    }
}
