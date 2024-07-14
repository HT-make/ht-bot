package com.htmake.htbot.discord.commands.quest.action;

import com.htmake.htbot.discord.commands.quest.cache.QuestDialogueCache;
import com.htmake.htbot.discord.commands.quest.data.DialogueData;
import com.htmake.htbot.discord.commands.quest.data.QuestDialogueData;
import com.htmake.htbot.discord.commands.quest.util.QuestUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.quest.enums.Trigger;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.global.generics.LimitedCapacityDeque;
import com.htmake.htbot.global.custom.interaction.enums.EventType;
import com.htmake.htbot.global.custom.interaction.factory.InteractionEventFactory;
import com.htmake.htbot.global.custom.interaction.wrapper.InteractionEventWrapper;
import com.htmake.htbot.global.unirest.records.RequestParam;
import com.htmake.htbot.global.unirest.records.RouteParam;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestDialogueAction {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;
    private final QuestUtil questUtil;

    private final QuestDialogueCache questDialogueCache;

    private User user;

    public QuestDialogueAction() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
        this.questUtil = new QuestUtil();

        this.questDialogueCache = CacheFactory.questDialogueCache;
    }

    public void execute(SlashCommandInteractionEvent event) {
        this.user = event.getUser();
        InteractionEventWrapper eventWrapper = InteractionEventFactory.createWrapper(event);
        execute(eventWrapper, Trigger.START);
    }

    public void execute(ButtonInteractionEvent event) {
        this.user = event.getUser();
        InteractionEventWrapper eventWrapper = InteractionEventFactory.createWrapper(event);
        execute(eventWrapper, Trigger.END);
    }

    private void execute(InteractionEventWrapper event, Trigger trigger) {
        HttpResponse<JsonNode> response = request(trigger);

        if (response.getStatus() == 200) {
            JSONObject questDialogueObject = response.getBody().getObject();
            requestSuccess(event, trigger, questDialogueObject);
        } else {
            if (event.getEventType().equals(EventType.SLASH)) {
                errorUtil.sendError(event.getSlashEvent(), "퀘스트를 불러올 수 없습니다.", "잠시 후 다시 이용해주세요.");
            } else {
                errorUtil.sendError(event.getButtonEvent().getHook(), "퀘스트를 불러올 수 없습니다.", "잠시 후 다시 이용해주세요.");
            }
        }
    }

    private HttpResponse<JsonNode> request(Trigger trigger) {
        String endPoint = "/quest/dialogue/{player_id}";
        RouteParam routeParam = new RouteParam("player_id", user.getId());
        RequestParam requestParam = new RequestParam("trigger", trigger.toString());
        return httpClient.sendGetRequest(endPoint, routeParam, requestParam);
    }

    private void requestSuccess(InteractionEventWrapper event, Trigger trigger, JSONObject questDialogueObject) {
        QuestDialogueData questDialogueData = saveCache(trigger, questDialogueObject);
        LimitedCapacityDeque<DialogueData> ongoingDialogue = questDialogueData.getOngoingDialogue();

        MessageEmbed embed = buildEmbed(ongoingDialogue);

        event.replyEmbed(embed)
                .setActionRow(
                        Button.primary("quest-dialogue-before", "이전").asDisabled(),
                        Button.primary("quest-dialogue-next", "다음"),
                        Button.danger("cancel", "닫기")
                )
                .queue();
    }

    private QuestDialogueData saveCache(Trigger trigger, JSONObject questDialogueObject) {
        List<DialogueData> dialogueList = toDialougeList(questDialogueObject);
        LimitedCapacityDeque<DialogueData> ongoingDialogue = new LimitedCapacityDeque<>(6);
        ongoingDialogue.addFirst(dialogueList.get(0));

        QuestDialogueData questDialogueData = QuestDialogueData.builder()
                .dialogueList(dialogueList)
                .ongoingDialogue(ongoingDialogue)
                .trigger(trigger)
                .build();

        questDialogueCache.put(user.getId(), questDialogueData);

        return questDialogueData;
    }

    private List<DialogueData> toDialougeList(JSONObject scriptObject) {
        List<DialogueData> dialogueList = new ArrayList<>();
        JSONArray dialogueArray = scriptObject.getJSONArray("dialogueList");

        for (int i = 0; i < dialogueArray.length(); i++) {
            JSONObject dialogueObject = dialogueArray.getJSONObject(i);

            DialogueData dialogue = DialogueData.builder()
                    .character(dialogueObject.getString("character"))
                    .dialogue(dialogueObject.getString("dialogue"))
                    .sequence(dialogueObject.getInt("sequence"))
                    .build();

            dialogue.setPlayerName(user.getName());

            dialogueList.add(dialogue);
        }

        return dialogueList;
    }

    private MessageEmbed buildEmbed(LimitedCapacityDeque<DialogueData> ongoingDialogue) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":scroll: 퀘스트")
                .setDescription(questUtil.convertDialogue(ongoingDialogue.findAll()))
                .build();
    }
}
