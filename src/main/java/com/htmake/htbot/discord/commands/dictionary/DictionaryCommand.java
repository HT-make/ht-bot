package com.htmake.htbot.discord.commands.dictionary;

import com.htmake.htbot.discord.commands.dictionary.event.DictionarySlashEvent;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class DictionaryCommand extends ListenerAdapter {

    private final DictionarySlashEvent dictionarySlashEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public DictionaryCommand() {
        this.dictionarySlashEvent = new DictionarySlashEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("도감")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            dictionarySlashEvent.execute(event);
        }
    }
}
