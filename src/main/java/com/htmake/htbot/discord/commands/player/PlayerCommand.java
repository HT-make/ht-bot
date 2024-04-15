package com.htmake.htbot.discord.commands.player;

import com.htmake.htbot.discord.commands.player.event.PlayerJoinEvent;
import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.global.unirest.HttpClient;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlayerCommand extends ListenerAdapter {

    private final HttpClient httpClient;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("게임-가입")) {
            handleGameJoin(event);
        } else if (command.equals("유저-정보")) {
            handleUserInfo(event);
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        List<String> components = List.of(event.getComponentId().split("-"));
        String component = components.get(0);

        if (component.equals("job")) {
            new PlayerJoinEvent(event, httpClient, components.get(1));
        }
    }

    private void handleGameJoin(SlashCommandInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":video_game: 게임 가입")
                .setDescription("직업을 선택해주세요!\n한 번 선택된 직업은 변경이 불가합니다!")
                .addField("전사", "기본 공격력이 높고, 몸이 튼튼한 밸런스형", true)
                .addField("궁수", "높은 치명타 확률과 데미지를 이용한 강력한 한방", true)
                .addField("마법사", "다양한 스킬을 이용한 전략적인 전투", true)
                .build();

        event.replyEmbeds(embed)
                .setActionRow(
                        Button.primary("job-warrior", "전사"),
                        Button.primary("job-archer", "궁수"),
                        Button.primary("job-wizard", "마법사")
                )
                .queue();
    }

    private void handleUserInfo(SlashCommandInteractionEvent event) {
        User user;
        OptionMapping option = event.getOption("유저");

        if (option == null) {
            user = event.getUser();
        } else {
            user = option.getAsUser();
        }

        String endPoint = "/player/info/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", user.getId());

        HttpResponse<JsonNode> response = httpClient.sendGetRequest(endPoint, routeParam);

        if (response.getStatus() == 200) {
            JSONObject playerData = response.getBody().getObject();
            int level = playerData.getInt("level");
            int currentExp = playerData.getInt("currentExp");
            int maxExp = playerData.getInt("maxExp");

            String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

            Job job = Job.toEnum(playerData.getString("job"));

            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setAuthor(user.getName(), null, profileUrl)
                    .setTitle("유저 정보")

                    .addField(":beginner: Lv. " + level, currentExp + "/" + maxExp, true)
                    .addField("직업", job.getName(), true)
                    .addBlankField(true)

                    .addField(":crossed_swords: 공격력", "" + playerData.getInt("damage"), true)
                    .addField(":heart: 체력", "" + playerData.getInt("health"), true)
                    .addField(":shield: 방어력", "" + playerData.getInt("defence"), true)

                    .addField(":large_blue_diamond: 마나", "" + playerData.getInt("mana"), true)
                    .addField(":boom: 치명타 확률", playerData.getInt("criticalChance") + "%", true)
                    .addField(":boom: 치명타 데미지", playerData.getInt("criticalDamage") + "%", true)

                    .addField("무기", playerData.getString("weaponName"), true)
                    .addField("갑옷", playerData.getString("armorName"), true)
                    .build();

            event.replyEmbeds(embed).queue();
        } else {
            MessageEmbed embed = new EmbedBuilder()
                    .setColor(Color.YELLOW)
                    .setTitle(":warning: 유저 정보")
                    .setDescription("해당 유저의 정보를 찾을 수 없습니다!")
                    .build();

            event.replyEmbeds(embed).queue();
            log.error(String.valueOf(response.getBody()));
        }
    }
}