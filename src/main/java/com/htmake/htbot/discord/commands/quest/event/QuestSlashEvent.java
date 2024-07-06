package com.htmake.htbot.discord.commands.quest.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.discord.util.FormatUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

public class QuestSlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public QuestSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());
        
        if (response.getStatus() == 200) {
            JSONObject questObject = response.getBody().getObject();
            requestSuccess(event, questObject);
        } else {
            errorUtil.sendError(event, "퀘스트를 불러올 수 없습니다.", "잠시 후 다시 이용해주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/quest/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, JSONObject questObject) {
        MessageEmbed embed = buildEmbed(questObject, event.getUser());

        Button completeButton = Button.primary("quest-complete", "완료").asDisabled();

        if (validTargetQuantity(questObject)) {
            completeButton = Button.primary("quest-complete", "완료").asEnabled();
        }

        event.replyEmbeds(embed)
                .addActionRow(
                        completeButton,
                        Button.danger("cancel", "닫기")
                )
                .queue();
    }

    private MessageEmbed buildEmbed(JSONObject questObject, User user) {
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


