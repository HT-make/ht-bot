package com.htmake.htbot.discord.commands.quest.event;

import com.htmake.htbot.discord.commands.quest.action.QuestCompleteAction;
import com.htmake.htbot.discord.commands.quest.action.QuestDetailAction;
import com.htmake.htbot.discord.commands.quest.cache.QuestDialogueCache;
import com.htmake.htbot.discord.commands.quest.data.DialogueData;
import com.htmake.htbot.discord.commands.quest.data.QuestDialogueData;
import com.htmake.htbot.discord.commands.quest.enums.QuestButtonType;
import com.htmake.htbot.discord.commands.quest.util.QuestUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.quest.enums.Trigger;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.global.generics.LimitedCapacityDeque;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class QuestDialogueButtonEvent {

    private final ErrorUtil errorUtil;
    private final QuestUtil questUtil;

    private final QuestDetailAction questDetailAction;
    private final QuestCompleteAction questCompleteAction;

    private final QuestDialogueCache questDialogueCache;

    public QuestDialogueButtonEvent() {
        this.errorUtil = new ErrorUtil();
        this.questUtil = new QuestUtil();

        this.questDetailAction = new QuestDetailAction();
        this.questCompleteAction = new QuestCompleteAction();

        this.questDialogueCache = CacheFactory.questDialogueCache;
    }

    public void execute(ButtonInteractionEvent event, String type) {
        QuestButtonType buttonType = QuestButtonType.toEnum(type);
        String playerId = event.getUser().getId();

        if (!questDialogueCache.containsKey(playerId)) {
            errorUtil.sendError(event.getHook(), "퀘스트", "대화 내용을 불러올 수 없습니다.");
        }

        QuestDialogueData questDialogue = questDialogueCache.get(playerId);

        switch (buttonType) {
            case NEXT -> nextButton(event, questDialogue);
            case BEFORE -> beforeButton(event, questDialogue);
            case CONFIRM -> confirmButton(event, questDialogue);
        }
    }

    private void nextButton(ButtonInteractionEvent event, QuestDialogueData questDialogue) {
        MessageEmbed originalEmbed = event.getMessage().getEmbeds().get(0);
        List<DialogueData> dialogueList = questDialogue.getDialogueList();
        LimitedCapacityDeque<DialogueData> ongoingDialogue = questDialogue.getOngoingDialogue();

        int sequence = ongoingDialogue.getLast().getSequence();
        ongoingDialogue.addLast(dialogueList.get(sequence));

        MessageEmbed newEmbed = buildEmbed(originalEmbed, ongoingDialogue);
        List<Button> buttonList = new ArrayList<>();

        buttonList.add(Button.primary("quest-dialogue-before", "이전"));

        if (sequence == dialogueList.size() - 1) {
            buttonList.add(Button.success("quest-dialogue-confirm", "완료"));
        } else {
            buttonList.add(Button.primary("quest-dialogue-next", "다음"));
        }

        buttonList.add(Button.danger("cancel", "닫기"));

        event.getHook().editOriginalEmbeds(newEmbed)
                .setComponents(ActionRow.of(buttonList))
                .queue();
    }

    private void beforeButton(ButtonInteractionEvent event, QuestDialogueData questDialogue) {
        MessageEmbed originalEmbed = event.getMessage().getEmbeds().get(0);
        List<DialogueData> dialogueList = questDialogue.getDialogueList();
        LimitedCapacityDeque<DialogueData> ongoingDialogue = questDialogue.getOngoingDialogue();

        int firstSequence = ongoingDialogue.getFirst().getSequence();
        int lastSequence = ongoingDialogue.getLast().getSequence();
        int currentSize = ongoingDialogue.size();
        int maxSize = ongoingDialogue.maxSize();

        if (currentSize < maxSize) {
            ongoingDialogue.removeLast();
        } else {
            ongoingDialogue.addFirst(dialogueList.get(firstSequence - 1));
        }

        MessageEmbed newEmbed = buildEmbed(originalEmbed, ongoingDialogue);
        Button beforeButton = Button.primary("quest-dialogue-before", "이전");

        if (lastSequence == 2) {
            beforeButton = Button.primary("quest-dialogue-before", "이전").asDisabled();
        }

        event.getHook().editOriginalEmbeds(newEmbed)
                .setActionRow(
                        beforeButton,
                        Button.primary("quest-dialogue-next", "다음"),
                        Button.danger("cancel", "닫기")
                )
                .queue();
    }

    private MessageEmbed buildEmbed(MessageEmbed originalEmbed, LimitedCapacityDeque<DialogueData> ongoingDialogue) {
        return new EmbedBuilder()
                .setColor(originalEmbed.getColor())
                .setAuthor(originalEmbed.getAuthor().getName(), null, originalEmbed.getAuthor().getIconUrl())
                .setTitle(originalEmbed.getTitle())
                .setDescription(questUtil.convertDialogue(ongoingDialogue.findAll()))
                .build();
    }

    private void confirmButton(ButtonInteractionEvent event, QuestDialogueData questDialogue) {
        Trigger trigger = questDialogue.getTrigger();

        if (trigger.equals(Trigger.START)) {
            questDetailAction.execute(event);
        } else {
            questCompleteAction.execute(event);
        }
    }
}
