package com.htmake.htbot.discord.commands.dungeon;

import com.htmake.htbot.discord.commands.dungeon.event.DungeonCloseButtonEvent;
import com.htmake.htbot.discord.commands.dungeon.event.DungeonEntrySelectEvent;
import com.htmake.htbot.discord.commands.dungeon.event.DungeonEntrySlashEvent;
import com.htmake.htbot.discord.commands.dungeon.event.NextDungeonEntryButtonEvent;
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

    public DungeonCommand() {
        this.dungeonEntrySlashEvent = new DungeonEntrySlashEvent();
        this.dungeonEntrySelectEvent = new DungeonEntrySelectEvent();

        this.nextDungeonEntryButtonEvent = new NextDungeonEntryButtonEvent();

        this.dungeonCloseButtonEvent = new DungeonCloseButtonEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("던전-입장")) {
            dungeonEntrySlashEvent.execute(event);
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        List<String> componentList = List.of(event.getValues().get(0).split("-"));

        if (componentList.get(0).equals("dungeon")) {
            if (componentList.get(1).equals("enter")) {
                dungeonEntrySelectEvent.execute(event, componentList.get(2));
            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("dungeon")) {
            if (componentList.get(1).equals("next")) {
                nextDungeonEntryButtonEvent.execute(event);
            } else if (componentList.get(1).equals("close")) {
                dungeonCloseButtonEvent.execute(event);
            }
        }
    }
}
