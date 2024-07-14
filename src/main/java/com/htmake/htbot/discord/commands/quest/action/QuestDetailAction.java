package com.htmake.htbot.discord.commands.quest.action;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.FormatUtil;
import com.htmake.htbot.global.custom.interaction.enums.EventType;
import com.htmake.htbot.global.custom.interaction.factory.InteractionEventFactory;
import com.htmake.htbot.global.custom.interaction.wrapper.InteractionEventWrapper;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

public class QuestDetailAction {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    private User user;

    public QuestDetailAction() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();

        this.user = null;
    }

    public void execute(SlashCommandInteractionEvent event) {
        this.user = event.getUser();
        InteractionEventWrapper eventWrapper = InteractionEventFactory.createWrapper(event);
        execute(eventWrapper);
    }

    public void execute(ButtonInteractionEvent event) {
        this.user = event.getUser();
        InteractionEventWrapper eventWrapper = InteractionEventFactory.createWrapper(event);
        execute(eventWrapper);
    }

    private void execute(InteractionEventWrapper event) {
        HttpResponse<JsonNode> response = request();

        if (response.getStatus() == 200) {
            JSONObject questObject = response.getBody().getObject();
            requestSuccess(event, questObject);
        } else {
            EventType eventType = event.getEventType();
            if (eventType.equals(EventType.SLASH)) {
                errorUtil.sendError(event.getSlashEvent(), "퀘스트를 불러올 수 없습니다.", "잠시 후 다시 이용해주세요.");
            } else {
                errorUtil.sendError(event.getButtonEvent().getHook(), "퀘스트를 불러올 수 없습니다.", "잠시 후 다시 이용해주세요.");
            }
        }
    }

    private HttpResponse<JsonNode> request() {
        String endPoint = "/quest/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", user.getId());
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(InteractionEventWrapper event, JSONObject questObject) {
        MessageEmbed embed = buildEmbed(questObject);

        Button completeButton = Button.primary("quest-complete", "완료").asDisabled();
        if (validTargetQuantity(questObject)) {
            completeButton = Button.primary("quest-complete", "완료").asEnabled();
        }

        event.replyEmbed(embed)
                .setActionRow(
                        completeButton,
                        Button.danger("cancel", "닫기")
                )
                .queue();
    }

    private MessageEmbed buildEmbed(JSONObject questObject) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":scroll: 퀘스트")
                .addField(questObject.getString("title"), questObject.getString("description"), false)
                .addField(":dart: 목표", targetFormat(questObject), false)
                .addField(":purse: 보상", rewardFormat(questObject), false)
                .build();
    }

    private String targetFormat(JSONObject questObject) {
        JSONArray targetArray = questObject.getJSONArray("targetList");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < targetArray.length(); i++) {
            JSONObject targetObject = targetArray.getJSONObject(i);
            sb.append(targetObject.getString("name")).append(" (")
                    .append(targetObject.getInt("currentQuantity")).append("/")
                    .append(targetObject.getInt("requiredQuantity")).append(")\n");
        }

        return sb.toString();
    }

    private String rewardFormat(JSONObject questObject) {
        JSONArray rewardArray = questObject.getJSONArray("rewardList");
        StringBuilder sb = new StringBuilder();

        sb.append(FormatUtil.decimalFormat(questObject.getInt("gold"))).append(" 골드\n")
                .append(FormatUtil.decimalFormat(questObject.getInt("exp"))).append(" 경험치\n");

        for (int i = 0; i < rewardArray.length(); i++) {
            JSONObject rewardObject = rewardArray.getJSONObject(i);
            sb.append(rewardObject.getString("name")).append(" ")
                    .append(rewardObject.getInt("quantity")).append("개\n");
        }

        return sb.toString();
    }

    private boolean validTargetQuantity(JSONObject questObject) {
        JSONArray targetArray = questObject.getJSONArray("targetList");

        for (int i = 0; i < targetArray.length(); i++) {
            JSONObject targetObject = targetArray.getJSONObject(i);

            int requiredQuantity = targetObject.getInt("requiredQuantity");
            int currentQuantity = targetObject.getInt("currentQuantity");

            if (currentQuantity < requiredQuantity) {
                return false;
            }
        }

        return true;
    }
}
