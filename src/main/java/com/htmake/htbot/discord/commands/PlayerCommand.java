package com.htmake.htbot.discord.commands;

import com.htmake.htbot.interfaces.Pair;
import com.htmake.htbot.unirest.HttpClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.text.DecimalFormat;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlayerCommand extends ListenerAdapter {

    private final HttpClient httpClient;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("게임-가입")) {
            handleGameJoin(event);
        } else if (command.equals("유저-정보")) {
            handleUserInfo(event);
        }
    }

    private void handleGameJoin(SlashCommandInteractionEvent event) {
        User user = event.getUser();

        String endPoint = "/player/join";
        String jsonBody = "{\"userId\":\"" + user.getId() + "\", " +
                "\"name\":\"" + user.getName() + "\"}";

        HttpResponse<JsonNode> response = httpClient.sendPostRequest(endPoint, jsonBody);

        if (response.getStatus() == 200) {
            String message = response.getBody().getObject().getString("message");
            event.reply(message).queue();
        } else {
            log.error(String.valueOf(response.getBody()));
        }
    }

    private void handleUserInfo(SlashCommandInteractionEvent event) {
        User user;
        OptionMapping option = event.getOption("유저");

        if (option == null) {
            user = event.getUser();
        } else {
            user = option.getAsUser();
        }

        String endPoint = "/player/info/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", user.getId());

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###");

            JSONObject responseBody = response.getBody().getObject();
            int level = responseBody.getInt("level");
            int currentExp = responseBody.getInt("currentExp");
            int maxExp = responseBody.getInt("maxExp");

            String levelString = String.valueOf(level);
            String currentExpFormatted = decimalFormat.format(currentExp);
            String maxExpFormatted = decimalFormat.format(maxExp);
            String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setAuthor(user.getName(), null, profileUrl)
                    .addField(":beginner:LV. " + levelString,
                            currentExpFormatted + "/" + maxExpFormatted,
                            false)
                    .build();

            event.replyEmbeds(embed).queue();
        } else {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.YELLOW)
                    .setTitle(":warning:유저 정보")
                    .setDescription("해당 유저의 정보를 찾을 수 없습니다!")
                    .build();

            event.replyEmbeds(embed).queue();
            log.error(String.valueOf(response.getBody()));
        }
    }
}