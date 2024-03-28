package com.htmake.htbot.discord.commands;

import com.htmake.htbot.discord.util.RestServiceType;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

@Component
public class PlayerCommand extends ListenerAdapter {

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
        String jsonBody = "{\"userId\":\"" + user.getId() + "\", " +
                "\"name\":\"" + user.getName() + "\"}";

        try {
            HttpResponse<JsonNode> response = Unirest
                    .post(RestServiceType.DEFAULT_URL + "/player/join")
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asJson();

            if (response.getStatus() == 200) {
                String message = response.getBody().getObject().getString("message");
                event.reply(message).queue();
            } else {
                System.out.println(response.getBody());
            }
        } catch (UnirestException e) {
            e.printStackTrace();
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

        try {
            HttpResponse<JsonNode> response = Unirest
                    .get(RestServiceType.DEFAULT_URL + "/player/info/{player_id}")
                    .routeParam("player_id", user.getId())
                    .asJson();

            if (response.getStatus() == 200) {
                DecimalFormat decimalFormat = new DecimalFormat("#,###");

                int level = response.getBody().getObject().getInt("level");
                int currentExp = response.getBody().getObject().getInt("currentExp");
                int maxExp = response.getBody().getObject().getInt("maxExp");

                String StringLevel = String.valueOf(level);
                String StringCurrentExp = decimalFormat.format(currentExp);
                String StringMaxExp = decimalFormat.format(maxExp);
                String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

                MessageEmbed embed = new EmbedBuilder()
                        .setColor(Color.GREEN)
                        .setAuthor(user.getName(), null, profileUrl)
                        .addField(":beginner:LV. " + StringLevel,
                                StringCurrentExp + "/" + StringMaxExp,
                                false)
                        .build();

                event.replyEmbeds(embed).queue();
            } else {
                System.out.println(response.getBody());
                event.reply("유저 정보를 찾지 못했습니다.").queue();
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = List.of(
                Commands.slash("게임-가입", "게임 기능을 사용하기 위해 가입합니다."),
                Commands.slash("유저-정보", "유저 정보를 검색합니다.").addOptions(selectUser())
        );

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }

    private OptionData selectUser() {
        return new OptionData(OptionType.USER, "유저", "정보 검색을 원하는 유저를 선택해 주세요!");
    }
}
