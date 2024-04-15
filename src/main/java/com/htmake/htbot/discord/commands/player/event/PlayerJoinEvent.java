package com.htmake.htbot.discord.commands.player.event;

import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.global.unirest.HttpClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.util.Collections;

@Slf4j
public class PlayerJoinEvent {

    private final HttpClient httpClient;

    public PlayerJoinEvent(ButtonInteractionEvent event, HttpClient httpClient, String component) {
        this.httpClient = httpClient;

        String job = switch (component) {
            case "warrior" -> Job.WARRIOR.name();
            case "archer" -> Job.ARCHER.name();
            case "wizard" -> Job.WIZARD.name();
            default -> null;
        };

        User user = event.getUser();

        String endPoint = "/player/join";
        String requestBody =
                "{\"userId\":\"" + user.getId() + "\", " +
                "\"name\":\"" + user.getName() + "\", " +
                "\"job\":\"" + job + "\"}";

        String message = joinPlayer(endPoint, requestBody);

        if (message != null) {
            successJoin(event, message);
        } else {
            errorJoin(event);
        }
    }

    private String joinPlayer(String endPoint, String requestBody) {
        HttpResponse<JsonNode> response = httpClient.sendPostRequest(endPoint, requestBody);

        if (response.getStatus() == 200) {
            return response.getBody().getObject().getString("message");
        } else {
            log.error(String.valueOf(response.getBody()));
            return null;
        }
    }

    private void successJoin(ButtonInteractionEvent event, String message) {
        MessageEmbed embed = event.getMessage().getEmbeds().get(0);

        MessageEmbed newEmbed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(embed.getTitle())
                .setDescription(message)
                .build();

        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(newEmbed)
                .queue();
    }

    private void errorJoin(ButtonInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: 게임 가입 오류")
                .setDescription("게입 가입에 실패하였습니다.")
                .build();

        event.getMessage().editMessageComponents(Collections.emptyList()).queue();
        event.getMessage().editMessageEmbeds(embed)
                .queue();
    }
}
