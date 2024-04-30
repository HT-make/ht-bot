package com.htmake.htbot.discord.commands.dictionary;

import com.htmake.htbot.discord.commands.dictionary.event.DictionarySlashEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class DictionaryCommand extends ListenerAdapter {

    private final DictionarySlashEvent dictionarySlashEvent;

    public DictionaryCommand() {
        this.dictionarySlashEvent = new DictionarySlashEvent();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("도감")) {
            dictionarySlashEvent.execute(event);
        }
    }
}
