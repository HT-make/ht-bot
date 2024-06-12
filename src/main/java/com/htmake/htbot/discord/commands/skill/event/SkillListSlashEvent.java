package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.discord.commands.skill.cache.SkillCache;
import com.htmake.htbot.discord.commands.skill.data.SkillList;
import com.htmake.htbot.discord.commands.skill.util.SkillEventUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;

import java.util.List;

public class SkillListSlashEvent {

    private final HttpClient httpClient;
    private final SkillEventUtil skillEventUtil;
    private final ErrorUtil errorUtil;

    private final SkillCache skillCache;

    public SkillListSlashEvent() {
        this.httpClient = new HttpClientImpl();
        this.skillEventUtil = new SkillEventUtil();
        this.errorUtil = new ErrorUtil();

        this.skillCache = CacheFactory.skillCache;
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
        skillEventUtil.saveSkillListCache(skillList, event.getUser());

        SkillList skillLists = skillCache.get(event.getUser().getId());
        MessageEmbed embed = skillEventUtil.skillListBuildEmbed(skillLists.getFirstSkillList(), event.getUser());

        List<Button> buttonList = skillEventUtil.skillButtonEmbed(1, "", "skill-list");

        event.replyEmbeds(embed).addActionRow(buttonList).queue();
    }
}
