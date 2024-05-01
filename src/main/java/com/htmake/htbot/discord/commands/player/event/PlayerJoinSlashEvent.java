package com.htmake.htbot.discord.commands.player.event;

import com.htmake.htbot.discord.util.ErrorUtil;
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

public class PlayerJoinSlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public PlayerJoinSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONObject checkObject = response.getBody().getObject();
            requestSuccess(event, checkObject);
        } else {
            errorUtil.sendError(event, "게임 가입", "게임 가입을 이용할 수 없습니다. 잠시 후 다시 이용해주세요.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/player/check/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, JSONObject checkObject) {
        String exists = checkObject.getString("exists");

        if (exists.equals("true")) {
            MessageEmbed embed = buildExistsPlayerEmbed();
            event.replyEmbeds(embed).queue();
        } else {
            MessageEmbed embed = buildPlayerJoinEmbed(event.getUser());
            event.replyEmbeds(embed)
                    .setActionRow(
                            Button.primary("player-job-WARRIOR", "전사"),
                            Button.primary("player-job-ARCHER", "궁수"),
                            Button.primary("player-job-WIZARD", "마법사")
                    )
                    .queue();
        }
    }

    private MessageEmbed buildExistsPlayerEmbed() {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":video_game: 게임 가입")
                .setDescription("이미 가입된 플레이어입니다.")
                .build();
    }

    private MessageEmbed buildPlayerJoinEmbed(User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":video_game: 게임 가입")
                .setDescription("직업을 선택해주세요!\n한 번 선택된 직업은 변경이 불가합니다!")
                .addField("전사", "기본 공격력이 높고, 몸이 튼튼한 밸런스형", true)
                .addField("궁수", "높은 치명타 확률과 데미지를 이용한 강력한 한방", true)
                .addField("마법사", "다양한 스킬을 이용한 전략적인 전투", true)
                .build();
    }
}
