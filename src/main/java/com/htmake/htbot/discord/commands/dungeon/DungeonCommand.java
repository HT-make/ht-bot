package com.htmake.htbot.discord.commands.dungeon;

import com.htmake.htbot.discord.commands.dungeon.event.bossDungeon.BossDungeonEntryButtonEvent;
import com.htmake.htbot.discord.commands.dungeon.event.bossDungeon.BossDungeonEntrySelectEvent;
import com.htmake.htbot.discord.commands.dungeon.event.bossDungeon.BossDungeonMainButtonEvent;
import com.htmake.htbot.discord.commands.dungeon.event.dungeon.DungeonSlashEvent;
import com.htmake.htbot.discord.commands.dungeon.event.fieldDungeon.FieldDungeonCloseButtonEvent;
import com.htmake.htbot.discord.commands.dungeon.event.fieldDungeon.FieldDungeonEntrySelectEvent;
import com.htmake.htbot.discord.commands.dungeon.event.fieldDungeon.FieldDungeonMainButtonEvent;
import com.htmake.htbot.discord.commands.dungeon.event.fieldDungeon.NextStageEntryButtonEvent;
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

    private final DungeonSlashEvent dungeonSlashEvent;

    private final FieldDungeonMainButtonEvent fieldDungeonMainButtonEvent;
    private final FieldDungeonEntrySelectEvent fieldDungeonEntrySelectEvent;
    private final NextStageEntryButtonEvent nextStageEntryButtonEvent;
    private final FieldDungeonCloseButtonEvent fieldDungeonCloseButtonEvent;

    private final BossDungeonMainButtonEvent bossDungeonMainButtonEvent;
    private final BossDungeonEntrySelectEvent bossDungeonEntrySelectEvent;
    private final BossDungeonEntryButtonEvent bossDungeonEntryButtonEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public DungeonCommand() {
        this.dungeonSlashEvent = new DungeonSlashEvent();

        this.fieldDungeonMainButtonEvent = new FieldDungeonMainButtonEvent();
        this.fieldDungeonEntrySelectEvent = new FieldDungeonEntrySelectEvent();
        this.nextStageEntryButtonEvent = new NextStageEntryButtonEvent();
        this.fieldDungeonCloseButtonEvent = new FieldDungeonCloseButtonEvent();

        this.bossDungeonMainButtonEvent = new BossDungeonMainButtonEvent();
        this.bossDungeonEntrySelectEvent = new BossDungeonEntrySelectEvent();
        this.bossDungeonEntryButtonEvent = new BossDungeonEntryButtonEvent();

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

            dungeonSlashEvent.execute(event);
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

            switch (componentList.get(1)) {
                case "field" -> fieldDungeonEntrySelectEvent.execute(event, componentList.get(2));
                case "boss" -> bossDungeonEntrySelectEvent.execute(event, componentList.get(2));
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

            switch (componentList.get(1)) {
                case "field" -> {
                    switch (componentList.get(2)) {
                        case "main" -> fieldDungeonMainButtonEvent.execute(event);
                        case "next" -> nextStageEntryButtonEvent.execute(event);
                        case "close" ->  {
                            messageUtil.remove(event.getUser().getId());
                            fieldDungeonCloseButtonEvent.execute(event);
                        }
                    }
                }
                case "boss" -> {
                    switch (componentList.get(2)) {
                        case "main" -> bossDungeonMainButtonEvent.execute(event);
                        case "entry" -> bossDungeonEntryButtonEvent.execute(event, componentList.get(3));
                    }
                }
            }
        }
    }
}
