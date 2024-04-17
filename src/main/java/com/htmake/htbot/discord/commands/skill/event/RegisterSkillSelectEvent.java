package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.discord.commands.skill.util.SkillEventUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.htmake.htbot.global.util.ObjectMapperUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegisterSkillSelectEvent {

    private final HttpClient httpClient;
    private final SkillEventUtil skillEventUtil;
    private final ObjectMapperUtil objectMapperUtil;

    public RegisterSkillSelectEvent() {
        this.httpClient = new HttpClientImpl();
        this.skillEventUtil = new SkillEventUtil();
        this.objectMapperUtil = new ObjectMapperUtil();
    }

    public void execute(StringSelectInteractionEvent event, Pair<String, String> value) {
        HttpResponse<JsonNode> response = request(event.getUser().getId(), value);

        if (response.getStatus() == 200) {
            requestSuccess(event);
        } else {
            skillEventUtil.errorMessage(event, "스킬 등록", "스킬을 등록 할 수 없습니다.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId, Pair<String, String> value) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("id", Long.valueOf(value.getFirst()));
        requestData.put("number", Integer.valueOf(value.getSecond()));

        String endPoint = "/skill/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        String jsonBody = objectMapperUtil.mapper(requestData);

        return httpClient.sendPostRequest(endPoint, routeParam, jsonBody);
    }

    private void requestSuccess(StringSelectInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":bookmark: 스킬 등록")
                .setDescription("스킬 등록에 성공했습니다!")
                .build();

        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(embed).queue();
    }
}
