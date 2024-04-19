package com.htmake.htbot.discord.commands.quest;

import com.htmake.htbot.discord.commands.quest.event.QuestCompleteButtonEvent;
import com.htmake.htbot.discord.commands.quest.event.QuestSlashEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestCommand extends ListenerAdapter {
    private final QuestSlashEvent questSlashEvent;

    private final QuestCompleteButtonEvent questCompleteButtonEvent;
    public QuestCommand() {
        this.questSlashEvent = new QuestSlashEvent();
        this.questCompleteButtonEvent = new QuestCompleteButtonEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("퀘스트")) {
            questSlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("quest")){
            if (componentList.get(1).equals("complete")) {
                questCompleteButtonEvent.execute(event);
            }
        }
    }
}
