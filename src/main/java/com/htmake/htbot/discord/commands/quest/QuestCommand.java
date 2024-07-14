package com.htmake.htbot.discord.commands.quest;

import com.htmake.htbot.discord.commands.quest.event.QuestCompleteButtonEvent;
import com.htmake.htbot.discord.commands.quest.event.QuestDialogueButtonEvent;
import com.htmake.htbot.discord.commands.quest.event.QuestSlashEvent;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.MessageUtil;
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

    private final QuestDialogueButtonEvent questDialogueButtonEvent;

    private final ErrorUtil errorUtil;
    private final MessageUtil messageUtil;

    public QuestCommand() {
        this.questSlashEvent = new QuestSlashEvent();

        this.questCompleteButtonEvent = new QuestCompleteButtonEvent();

        this.questDialogueButtonEvent = new QuestDialogueButtonEvent();

        this.errorUtil = new ErrorUtil();
        this.messageUtil = new MessageUtil();
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("퀘스트")) {
            if (messageUtil.validCheck(event.getUser().getId())) {
                errorUtil.sendError(event, "작업 실패", "현재 다른 작업을 진행중입니다.");
                return;
            }

            questSlashEvent.execute(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> componentList = List.of(event.getComponentId().split("-"));

        if (componentList.get(0).equals("quest")){
            if (messageUtil.validCheck(event.getMessage(), event.getUser().getName())) {
                errorUtil.sendError(event.getHook(), "제한된 버튼", "이 버튼은 이용할 수 없습니다.");
                return;
            }

            if (componentList.get(1).equals("complete")) {
                questCompleteButtonEvent.execute(event);
            }

            switch (componentList.get(1)) {
                case "complete" -> questCompleteButtonEvent.execute(event);
                case "dialogue" -> questDialogueButtonEvent.execute(event, componentList.get(2));
            }
        }
    }
}
