package com.htmake.htbot.discord.commands.player;

import com.htmake.htbot.discord.commands.player.event.PlayerInfoSlashEvent;
import com.htmake.htbot.discord.commands.player.event.PlayerJoinButtonEvent;
import com.htmake.htbot.discord.commands.player.event.PlayerJoinSlashEvent;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
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

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public PlayerCommand() {
        this.playerJoinSlashEvent = new PlayerJoinSlashEvent();
        this.playerJoinButtonEvent = new PlayerJoinButtonEvent();

        this.playerInfoSlashEvent = new PlayerInfoSlashEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("게임-가입")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            playerJoinSlashEvent.execute(event);
        } else if (command.equals("유저-정보")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            playerInfoSlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("player")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            if (componentList.get(1).equals("job")) {
                playerJoinButtonEvent.execute(event, componentList.get(2));
            }
        }
    }
}