package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.discord.commands.skill.util.SkillEventUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.json.JSONArray;

import java.awt.*;
import java.util.List;

public class SkillListSlashEvent {

    private final HttpClient httpClient;
    private final SkillEventUtil skillEventUtil;
    private final ErrorUtil errorUtil;

    public SkillListSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.skillEventUtil = new SkillEventUtil();
        this.errorUtil = new ErrorUtil();
    }

    public void execute(SlashCommandInteractionEvent event) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONArray skillArray = response.getBody().getObject().getJSONArray("skillResponseList");
            requestSuccess(event, skillArray);
        } else {
            errorUtil.sendError(event, "스킬 목록", "스킬 목록을 불러올 수 없습니다.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/skill/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(SlashCommandInteractionEvent event, JSONArray skillArray) {
        List<SkillResponse> skillList = skillEventUtil.toSkillList(skillArray);
        MessageEmbed embed = buildEmbed(skillList, event.getUser());
        event.replyEmbeds(embed).queue();
    }

    private MessageEmbed buildEmbed(List<SkillResponse> skillList, User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":bookmark: 스킬 목록")
                .setDescription("보유중인 스킬 목록을 보여줍니다.");

        for (SkillResponse skill : skillList) {
            String title = skill.getName();

            if (skill.getIsRegistered().equals("true")) {
                title += "(등록됨)";
            }

            embedBuilder.addField(title, skill.getDescription(), false);
        }

        return embedBuilder.build();
    }
}
