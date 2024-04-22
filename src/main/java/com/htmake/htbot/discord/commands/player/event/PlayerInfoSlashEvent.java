package com.htmake.htbot.discord.commands.player.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.player.presentation.data.response.PlayerInfoResponse;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.json.JSONObject;

import java.awt.*;

public class PlayerInfoSlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public PlayerInfoSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        User user;
        OptionMapping option = event.getOption("유저");

        if (option == null) {
            user = event.getUser();
        } else {
            user = option.getAsUser();
        }

        HttpResponse<JsonNode> response = request(user.getId());

        if (response.getStatus() == 200) {
            JSONObject playerObject = response.getBody().getObject();
            requestSuccess(event, user, playerObject);
        } else {
            errorUtil.sendError(event, "유저 정보", "플레이어를 찾을 수 없습니다.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/player/info/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, User user, JSONObject playerInfoObject) {
        PlayerInfoResponse playerInfo = toPlayerInfo(playerInfoObject);
        MessageEmbed embed = buildEmbed(user, playerInfo);

        event.replyEmbeds(embed).queue();
    }

    private PlayerInfoResponse toPlayerInfo(JSONObject playerInfoObject) {
        return PlayerInfoResponse.builder()
                .level(playerInfoObject.getInt("level"))
                .currentExp(playerInfoObject.getInt("currentExp"))
                .maxExp(playerInfoObject.getInt("maxExp"))
                .damage(playerInfoObject.getInt("damage"))
                .health(playerInfoObject.getInt("health"))
                .defence(playerInfoObject.getInt("defence"))
                .mana(playerInfoObject.getInt("mana"))
                .criticalChance(playerInfoObject.getInt("criticalChance"))
                .criticalDamage(playerInfoObject.getInt("criticalDamage"))
                .job(playerInfoObject.getString("job"))
                .weaponName(playerInfoObject.getString("weaponName"))
                .armorName(playerInfoObject.getString("armorName"))
                .build();
    }

    private MessageEmbed buildEmbed(User user, PlayerInfoResponse playerInfo) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();
        String job = Job.toEnum(playerInfo.getJob()).getName();

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle("유저 정보")

                .addField(":beginner: Lv. " + playerInfo.getLevel(), playerInfo.getCurrentExp() + "/" + playerInfo.getMaxExp(), true)
                .addField("직업", job, true)
                .addBlankField(true)

                .addField(":crossed_swords: 공격력", "" + playerInfo.getDamage(), true)
                .addField(":heart: 체력", "" + playerInfo.getHealth(), true)
                .addField(":shield: 방어력", "" + playerInfo.getDefence(), true)

                .addField(":large_blue_diamond: 마나", "" + playerInfo.getMana(), true)
                .addField(":boom: 치명타 확률", playerInfo.getCriticalChance() + "%", true)
                .addField(":boom: 치명타 데미지", playerInfo.getCriticalDamage() + "%", true)

                .addField("무기", playerInfo.getWeaponName(), true)
                .addField("갑옷", playerInfo.getArmorName(), true)
                .build();
    }
}
