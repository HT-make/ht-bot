package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.htmake.htbot.global.util.ObjectMapperUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RegisterSkillEvent {

    private final HttpClient httpClient;
    private final ObjectMapperUtil objectMapperUtil;

    public RegisterSkillEvent() {
        this.httpClient = new HttpClientImpl();
        this.objectMapperUtil = new ObjectMapperUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        OptionMapping nameOption = event.getOption("스킬이름");
        String name = nameOption.getAsString();

        HttpResponse<JsonNode> response = request(event.getUser().getId(), name);

        if (response.getStatus() == 200) {
            successRegisterSkill(event);
        } else {
            String message = response.getBody().getObject().getString("message");
            errorMessage(event, message);
        }
    }

    private HttpResponse<JsonNode> request(String playerId, String name) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("name", name);

        String endPoint = "/skill/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        String jsonBody = objectMapperUtil.mapper(requestData);

        return httpClient.sendPostRequest(endPoint, routeParam, jsonBody);
    }

    private void successRegisterSkill(SlashCommandInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("스킬 등록")
                .setDescription("스킬 등록에 성공했습니다!")
                .build();

        event.replyEmbeds(embed).queue();
    }

    private void errorMessage(SlashCommandInteractionEvent event, String message) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: 스킬 등록")
                .setDescription(message)
                .build();

        event.replyEmbeds(embed).queue();
    }
}
