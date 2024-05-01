package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.skill.presentation.data.response.RegisteredSkillResponse;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RegisterSkillSlashEvent {

    private final HttpClient httpClient;
    private final ErrorUtil errorUtil;

    public RegisterSkillSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONArray skillArray = response.getBody().getObject().getJSONArray("skillResponseList");
            requestSuccess(event, skillArray);
        } else {
            errorUtil.sendError(event, "스킬 등록", "스킬 목록을 불러올 수 없습니다.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/skill/registered/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);

        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, JSONArray skillArray) {
        List<RegisteredSkillResponse> skillList = toSkillList(skillArray);
        MessageEmbed embed = buildEmbed(skillList, event.getUser());

        event.replyEmbeds(embed)
                .addActionRow(
                        Button.primary("skill-register-1", "스킬1"),
                        Button.primary("skill-register-2", "스킬2"),
                        Button.primary("skill-register-3", "스킬3")
                )
                .addActionRow(
                        Button.primary("skill-register-4", "스킬4"),
                        Button.primary("skill-register-5", "스킬5"),
                        Button.danger("cancel", "닫기")
                )
                .queue();
    }

    private List<RegisteredSkillResponse> toSkillList(JSONArray skillArray) {
        List<RegisteredSkillResponse> skillList = new ArrayList<>();

        for (int i = 0; i < skillArray.length(); i++) {
            JSONObject skillObject = skillArray.getJSONObject(i);

            RegisteredSkillResponse skill = RegisteredSkillResponse.builder()
                    .number(skillObject.getInt("number"))
                    .name(skillObject.getString("name"))
                    .build();

            skillList.add(skill);
        }

        return skillList;
    }

    private MessageEmbed buildEmbed(List<RegisteredSkillResponse> skillList, User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":bookmark: 스킬 등록")
                .setDescription("스킬을 등록할 곳을 선택해 주세요.");

        skillList.sort(Comparator.comparingInt(RegisteredSkillResponse::getNumber));

        for (int i = 1; i <= 5; i++) {
            String message = "비어있습니다.";

            if (!skillList.isEmpty()) {
                RegisteredSkillResponse skill = skillList.get(0);

                if (skill.getNumber() == i) {
                    message = skill.getName();
                    skillList.remove(0);
                }
            }

            embedBuilder.addField("스킬" + i, message, true);
        }

        return embedBuilder.build();
    }
}
