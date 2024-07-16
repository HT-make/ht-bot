package com.htmake.htbot.discord.commands.global.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONObject;

import java.awt.*;

public class GlobalSlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public GlobalSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONObject checkObject = response.getBody().getObject();
            requestSuccess(event, checkObject);
        } else {
            errorUtil.sendError(event, "서버 오류", "현재 디스코드 봇을 이용할 수 없습니다.\n잠시 후 다시 이용해주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/player/check/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, JSONObject checkObject) {
        String exists = checkObject.getString("exists");

        if (exists.equals("false")) {
            MessageEmbed embed = buildEmbed();
            event.replyEmbeds(embed).queue();
        }
    }

    private MessageEmbed buildEmbed() {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":video_game: HT봇이 처음이신가요?")
                .setDescription("HT봇을 이용하기 위해서 `/게임-가입`을 통해 가입 해주세요.")
                .build();
    }
}
