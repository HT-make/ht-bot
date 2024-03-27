package com.htmake.htbot.discord.commands;

import com.htmake.htbot.discord.util.RestServiceType;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        User user = event.getUser();

        if (command.equals("게임-가입")) {
            String jsonBody = "{\"userId\":\"" + user.getId() + "\", " +
                    "\"name\":\"" + user.getName() + "\"}";

            try {
                HttpResponse<JsonNode> response = Unirest
                        .post( RestServiceType.DEFAULT_URL + "/player/join")
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
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        List<CommandData> commandData = List.of(
                Commands.slash("게임-가입", "게임 기능을 사용하기 위해 가입합니다!")
        );

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
