package com.htmake.htbot.discord.commands.dungeon.event.fieldDungeon;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.discord.commands.dungeon.cache.FieldDungeonStatusCache;
import com.htmake.htbot.discord.commands.dungeon.data.FieldDungeonStatus;
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

public class FieldDungeonCloseButtonEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;
    private final ObjectMapperUtil objectMapperUtil;

    private final FieldDungeonStatusCache fieldDungeonStatusCache;

    public FieldDungeonCloseButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
        this.objectMapperUtil = new ObjectMapperUtil();

        this.fieldDungeonStatusCache = CacheFactory.fieldDungeonStatusCache;
    }

    public void execute(ButtonInteractionEvent event) {
        String playerId = event.getUser().getId();

        FieldDungeonStatus fieldDungeonStatus = fieldDungeonStatusCache.get(playerId);
        List<GetItem> getItemList = fieldDungeonStatus.getGetItemList();

        if (!getItemList.isEmpty())  {
            HttpResponse<JsonNode> response = request(getItemList, playerId);

            if (response.getStatus() == 200) {
                requestSuccess(event);
                fieldDungeonStatusCache.remove(playerId);
            } else {
                errorUtil.sendError(event.getHook(), "던전 퇴장", "던전 퇴장에 실패하였습니다.");
            }
        }
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

        event.getHook().editOriginalComponents(Collections.emptyList()).queue();
        event.getHook().editOriginalEmbeds(embed).queue();
    }
}
