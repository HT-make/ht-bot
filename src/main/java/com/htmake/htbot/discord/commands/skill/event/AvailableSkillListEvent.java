package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillResponse;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AvailableSkillListEvent {

    private final HttpClient httpClient;

    public AvailableSkillListEvent() {
        this.httpClient = new HttpClientImpl();
    }

    public void execute(SlashCommandInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONArray skillArray = response.getBody().getObject().getJSONArray("skillResponseList");
            successGetList(event, skillArray);
        } else {
            errorMessage(event);
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/skill/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void successGetList(SlashCommandInteractionEvent event, JSONArray skillArray) {
        List<AvailableSkillResponse> skillList = toSkillList(skillArray);

        MessageEmbed embed = buildEmbed(skillList);

        event.replyEmbeds(embed).queue();
    }

    private List<AvailableSkillResponse> toSkillList(JSONArray skillArray) {
        List<AvailableSkillResponse> skillList = new ArrayList<>();

        for (int i = 0; i < skillArray.length(); i++) {
            JSONObject skillObject = skillArray.getJSONObject(i);

            AvailableSkillResponse skill = AvailableSkillResponse.builder()
                    .name(skillObject.getString("name"))
                    .value(skillObject.getInt("value"))
                    .mana(skillObject.getInt("mana"))
                    .skillType(skillObject.getString("skillType"))
                    .isRegistered(skillObject.getString("isRegistered"))
                    .build();

            skillList.add(skill);
        }

        return skillList;
    }

    private MessageEmbed buildEmbed(List<AvailableSkillResponse> skillList) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":bookmark: 스킬 목록")
                .setDescription("보유중인 스킬 목록을 보여줍니다.");

        for (AvailableSkillResponse skill : skillList) {
            String title = skill.getName();

            if (skill.getIsRegistered().equals("true")) {
                title += "(등록됨)";
            }

            embedBuilder.addField(title, format(skill), false);
        }

        return embedBuilder.build();
    }

    private String format(AvailableSkillResponse skill) {
        String skillType = skill.getSkillType().equals("ATTACK") ? "데미지" : "치유량";
        return String.format("%s: %d%% 마나: %d", skillType, skill.getValue(), skill.getMana());
    }


    private void errorMessage(SlashCommandInteractionEvent event) {
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(":warning: 스킬 목록")
                .setDescription("스킬 목록을 불러올 수 없습니다.")
                .build();

        event.replyEmbeds(embed).queue();
    }
}
