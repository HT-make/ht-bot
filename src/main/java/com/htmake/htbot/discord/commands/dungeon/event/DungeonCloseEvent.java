package com.htmake.htbot.discord.commands.dungeon.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.htmake.htbot.global.cache.Caches;
import com.htmake.htbot.discord.commands.dungeon.cache.DungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonStatus;
import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DungeonCloseEvent {

    private final HttpClient httpClient;

    private final DungeonStatusCache dungeonStatusCache;

    public DungeonCloseEvent() {
        this.httpClient = new HttpClientImpl();

        this.dungeonStatusCache = Caches.dungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        DungeonStatus dungeonStatus = dungeonStatusCache.get(playerId);
        List<GetItem> getItemList = dungeonStatus.getGetItemList();

        if (!getItemList.isEmpty())  {
            HttpResponse<JsonNode> response = insertItemRequest(getItemList, playerId);

            if (response.getStatus() == 200) {
                handleSuccessResponse(event);
            } else {
                handleErrorResponse(event, response);
            }
        }

        dungeonStatusCache.remove(playerId);
    }

    private HttpResponse<JsonNode> insertItemRequest(List<GetItem> getItemList, String playerId) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> requestData = new HashMap<>();

        requestData.put("dropItemList", getItemList);

        String endPoint = "/inventory/insert/dropItem/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        String requestBody;

        try {
            requestBody = objectMapper.writeValueAsString(requestData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return httpClient.sendPostRequest(endPoint, routeParam, requestBody);
    }

    private void handleSuccessResponse(ButtonInteractionEvent event) {
        Message message = event.getMessage();

        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("던전 퇴장")
                .setDescription("던전에서 퇴장하였습니다!")
                .build();

        message.editMessageComponents(Collections.emptyList()).queue();
        message.editMessageEmbeds(embed).queue();
    }

    private void handleErrorResponse(ButtonInteractionEvent event, HttpResponse<JsonNode> response) {
        Message message = event.getMessage();

        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: 던전 퇴장")
                .setDescription("던전 퇴장에 실패하였습니다!")
                .build();

        message.editMessageComponents(Collections.emptyList()).queue();
        message.editMessageEmbeds(embed).queue();

        log.error(String.valueOf(response.getBody()));
    }
}
