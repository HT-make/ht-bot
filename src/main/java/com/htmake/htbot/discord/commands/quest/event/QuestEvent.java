package com.htmake.htbot.discord.commands.quest.event;

import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONObject;

import java.awt.*;

public class QuestEvent{
    private final HttpClient httpClient;

    public QuestEvent() {
        this.httpClient = new HttpClientImpl();
    }

    public void execute(SlashCommandInteractionEvent event) {
        User user = event.getUser();

        String endPoint = "/quest/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", user.getId());

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        JSONObject questData = response.getBody().getObject();

        MessageEmbed embed = buildEmbed(questData);

        event.replyEmbeds(embed).queue();
    }

    private MessageEmbed buildEmbed(JSONObject questData) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle("진행중인 퀘스트 정보");
        embedBuilder.setDescription(format(questData));

        return embedBuilder.build();
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
                .append("골드: ").append(questData.getInt("gold")).append("\n")
                .append("경험치: ").append(questData.getInt("exp")).append("\n")
                .append("아이템: ").append(questData.getString("rewardItemName")).append(" (")
                .append(questData.getInt("rewardItemQuantity")).append(")\n");

        return sb.toString();
    }

}


