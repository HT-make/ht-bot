package com.htmake.htbot.discord.commands.quest.event;

import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;

import java.awt.*;
import java.util.ArrayList;

public class QuestCompleteEvent {
    private final HttpClient httpClient;

    public QuestCompleteEvent() {
        this.httpClient = new HttpClientImpl();
    }

    public void execute(ButtonInteractionEvent event) {
        User user = event.getUser();

        String endPoint = "/quest/progress/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", user.getId());

        HttpResponse<JsonNode> response = httpClient.sendPatchRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setTitle(":tada: 퀘스트를 완료했습니다.")
                    .setDescription("다음 퀘스트를 확인해 주세요.")
                    .build();

            event.getMessage().editMessageEmbeds(embed).queue();

            ArrayList<ActionRow> actionRows = new ArrayList<>(event.getMessage().getActionRows());
            actionRows.remove(0);

            event.getMessage().editMessageComponents(actionRows).queue();
        } else {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setTitle(":warning: 퀘스트를 완료하지 못했습니다.")
                    .setDescription("목표를 달성하고 다시 시도해 주세요.")
                    .build();

            event.getMessage().editMessageEmbeds(embed).queue();
        }
    }
}
