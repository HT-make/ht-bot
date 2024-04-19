package com.htmake.htbot.discord.commands.quest;

import com.htmake.htbot.discord.commands.quest.event.QuestCompleteEvent;
import com.htmake.htbot.discord.commands.quest.event.QuestEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestCommand extends ListenerAdapter {
    private final QuestEvent questEvent;

    private final QuestCompleteEvent questCompleteEvent;
    public QuestCommand() {
        this.questEvent = new QuestEvent();
        this.questCompleteEvent = new QuestCompleteEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("퀘스트")) {
            questEvent.execute(event);
        }
    }


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("quest")){
            if (componentList.get(1).equals("complete")) {
                questCompleteEvent.execute(event);
            }
        }
    }
}
