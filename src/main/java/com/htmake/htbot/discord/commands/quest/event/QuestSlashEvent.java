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
            JSONObject questData = response.getBody().getObject();
            requestSuccess(event, questData);
        } else {
            errorUtil.sendError(event, "퀘스트를 불러올 수 없습니다.", "잠시 후 다시 이용해주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/quest/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, JSONObject questData) {
        MessageEmbed embed = buildEmbed(questData, event.getUser());

        int monsterQuantity = questData.getInt("monsterQuantity");
        int targetMonsterQuantity = questData.getInt("targetMonsterQuantity");
        int itemQuantity = questData.getInt("itemQuantity");
        int targetItemQuantity = questData.getInt("targetItemQuantity");

        Button completeButton = Button.primary("quest-complete", "완료").asDisabled();

        if (monsterQuantity >= targetMonsterQuantity && itemQuantity >= targetItemQuantity) {
            completeButton = Button.primary("quest-complete", "완료").asEnabled();
        }

        event.replyEmbeds(embed)
                .addActionRow(
                        completeButton,
                        Button.danger("cancel", "닫기")
                )
                .queue();
    }

    private MessageEmbed buildEmbed(JSONObject questData, User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle("진행중인 퀘스트 정보")
                .setDescription(format(questData)).build();
    }

    private String format(JSONObject questData){
        StringBuilder sb = new StringBuilder();

        sb.append(":scroll: ").append(questData.getString("title")).append("\n")
                .append(questData.getString("description")).append("\n\n")
                .append(":dart: 목표").append("\n")
                .append("몬스터: ").append(questData.getString("targetMonster")).append(" (")
                .append(questData.getInt("monsterQuantity")).append("/").append(questData.getInt("targetMonsterQuantity")).append(")\n")
                .append("아이템: ").append(questData.getString("targetItem")).append(" (")
                .append(questData.getInt("itemQuantity")).append("/").append(questData.getInt("targetItemQuantity")).append(")\n\n")
                .append(":purse: 보상\n")
                .append("골드: ").append(FormatUtil.decimalFormat(questData.getInt("gold"))).append("\n")
                .append("경험치: ").append(FormatUtil.decimalFormat(questData.getInt("exp"))).append("\n")
                .append("아이템: ").append(questData.getString("rewardItemName")).append(" (")
                .append(questData.getInt("rewardItemQuantity")).append(")\n");

        return sb.toString();
    }
}


