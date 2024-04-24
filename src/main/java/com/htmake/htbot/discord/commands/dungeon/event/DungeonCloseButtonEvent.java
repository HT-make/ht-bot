package com.htmake.htbot.discord.commands.dungeon.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.cache.Caches;
import com.htmake.htbot.discord.commands.dungeon.cache.DungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.DungeonStatus;
import com.htmake.htbot.discord.commands.dungeon.data.GetItem;
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
import java.util.List;
import java.util.Map;

public class DungeonCloseButtonEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;
    private final ObjectMapperUtil objectMapperUtil;

    private final DungeonStatusCache dungeonStatusCache;

    public DungeonCloseButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
        this.objectMapperUtil = new ObjectMapperUtil();

        this.dungeonStatusCache = Caches.dungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        DungeonStatus dungeonStatus = dungeonStatusCache.get(playerId);
        List<GetItem> getItemList = dungeonStatus.getGetItemList();

        if (!getItemList.isEmpty())  {
            HttpResponse<JsonNode> response = request(getItemList, playerId);

            if (response.getStatus() == 200) {
                requestSuccess(event);
            } else {
                errorUtil.sendError(event.getMessage(), "던전 퇴장", "던전 퇴장에 실패하였습니다.");
            }
        }

        dungeonStatusCache.remove(playerId);
    }

    private HttpResponse<JsonNode> request(List<GetItem> getItemList, String playerId) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("dropItemList", getItemList);

        String endPoint = "/inventory/insert/dropItem/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        String requestBody = objectMapperUtil.mapper(requestData);

        return httpClient.sendPostRequest(endPoint, routeParam, requestBody);
    }

    private void requestSuccess(ButtonInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("던전 퇴장")
                .setDescription("던전에서 퇴장하였습니다.")
                .build();

        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(embed).queue();
    }
}
