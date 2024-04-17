package com.htmake.htbot.discord.commands.quest;

import com.htmake.htbot.discord.commands.quest.event.QuestCompleteEvent;
import com.htmake.htbot.discord.commands.quest.event.QuestEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

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
        } else if (command.equals("퀘스트-완료")) {
            questCompleteEvent.execute(event);
        }
    }
}
