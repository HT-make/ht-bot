package com.htmake.htbot.discord.commands.equipment.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.ObjectMapperUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EquipmentEquipSlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;
    private final ObjectMapperUtil objectMapperUtil;

    public EquipmentEquipSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
        this.objectMapperUtil = new ObjectMapperUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        String name = event.getOption("장비이름").getAsString();

        HttpResponse<JsonNode> response = request(event.getUser().getId(), name);

        if (response.getStatus() == 200) {
            requestSuccess(event);
        } else {
            String message = response.getBody().getObject().getString("message");
            errorUtil.sendError(event, "장비 장착", message);
        }
    }

    private HttpResponse<JsonNode> request(String playerId, String name) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("name", name);

        String endPoint = "/player/equip/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        String jsonBody = objectMapperUtil.mapper(requestData);

        return httpClient.sendPatchRequest(endPoint, routeParam, jsonBody);
    }

    private void requestSuccess(SlashCommandInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("장착 성공")
                .setDescription("장비 장착에 성공했습니다.")
                .build();

        event.replyEmbeds(embed).queue();
    }
}
