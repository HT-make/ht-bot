package com.htmake.htbot.discord.commands.player;

import com.htmake.htbot.discord.commands.player.event.PlayerInfoSlashEvent;
import com.htmake.htbot.discord.commands.player.event.PlayerJoinButtonEvent;
import com.htmake.htbot.discord.commands.player.event.PlayerJoinSlashEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerCommand extends ListenerAdapter {

    private final PlayerJoinSlashEvent playerJoinSlashEvent;
    private final PlayerJoinButtonEvent playerJoinButtonEvent;

    private final PlayerInfoSlashEvent playerInfoSlashEvent;

    public PlayerCommand() {
        this.playerJoinSlashEvent = new PlayerJoinSlashEvent();
        this.playerJoinButtonEvent = new PlayerJoinButtonEvent();

        this.playerInfoSlashEvent = new PlayerInfoSlashEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("게임-가입")) {
            playerJoinSlashEvent.execute(event);
        } else if (command.equals("유저-정보")) {
            playerInfoSlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("player")) {
            if (componentList.get(1).equals("job")) {
                playerJoinButtonEvent.execute(event, componentList.get(2));
            }
        }
    }
}