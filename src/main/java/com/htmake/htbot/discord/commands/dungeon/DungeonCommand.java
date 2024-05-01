package com.htmake.htbot.discord.commands.dungeon;

import com.htmake.htbot.discord.commands.dungeon.event.DungeonCloseButtonEvent;
import com.htmake.htbot.discord.commands.dungeon.event.DungeonEntrySelectEvent;
import com.htmake.htbot.discord.commands.dungeon.event.DungeonEntrySlashEvent;
import com.htmake.htbot.discord.commands.dungeon.event.NextDungeonEntryButtonEvent;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DungeonCommand extends ListenerAdapter {

    private final DungeonEntrySlashEvent dungeonEntrySlashEvent;
    private final DungeonEntrySelectEvent dungeonEntrySelectEvent;

    private final NextDungeonEntryButtonEvent nextDungeonEntryButtonEvent;

    private final DungeonCloseButtonEvent dungeonCloseButtonEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public DungeonCommand() {
        this.dungeonEntrySlashEvent = new DungeonEntrySlashEvent();
        this.dungeonEntrySelectEvent = new DungeonEntrySelectEvent();

        this.nextDungeonEntryButtonEvent = new NextDungeonEntryButtonEvent();

        this.dungeonCloseButtonEvent = new DungeonCloseButtonEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("던전-입장")) {
            String playerId = event.getUser().getId();

            if (messageUtil.validCheck(playerId)) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            messageUtil.put(playerId);

            dungeonEntrySlashEvent.execute(event);
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        List<String> componentList = List.of(event.getValues().get(0).split("-"));

        if (componentList.get(0).equals("dungeon")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            if (componentList.get(1).equals("enter")) {
                dungeonEntrySelectEvent.execute(event, componentList.get(2));
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("dungeon")) {
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            if (componentList.get(1).equals("next")) {
                nextDungeonEntryButtonEvent.execute(event);
            } else if (componentList.get(1).equals("close")) {
                messageUtil.remove(event.getUser().getId());
                dungeonCloseButtonEvent.execute(event);
            }
        }
    }
}
