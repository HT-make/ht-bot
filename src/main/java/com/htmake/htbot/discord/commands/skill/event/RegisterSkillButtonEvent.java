package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.discord.commands.skill.cache.SkillCache;
import com.htmake.htbot.discord.commands.skill.util.SkillEventUtil;
import com.htmake.htbot.discord.util.ErrorUtil;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import com.htmake.htbot.global.cache.CacheFactory;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.json.JSONArray;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterSkillButtonEvent {

    private final HttpClient httpClient;
    private final SkillEventUtil skillEventUtil;
    private final ErrorUtil errorUtil;

    private final SkillCache skillCache;

    public RegisterSkillButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.skillEventUtil = new SkillEventUtil();
        this.errorUtil = new ErrorUtil();

        this.skillCache = CacheFactory.skillCache;
    }

    public void execute(ButtonInteractionEvent event, String number) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONArray skillArray = response.getBody().getObject().getJSONArray("skillResponseList");
            requestSuccess(event, skillArray, number);
        } else {
            errorUtil.sendError(event.getHook(), "스킬 등록", "스킬 목록을 불러오지 못했습니다.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/skill/not/registered/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONArray skillArray, String number) {
        List<SkillResponse> skillList = skillEventUtil.toSkillList(skillArray);
        skillEventUtil.saveSkillListCache(skillList, event.getUser());

        List<ActionRow> actionRowList = new ArrayList<>();

        List<Button> buttonList = skillEventUtil.skillButtonEmbed(1, number, "skill-enroll");
        actionRowList.add(ActionRow.of(buttonList));

        List<SkillResponse> firstSkillList = skillCache.get(event.getUser().getId()).getFirstSkillList();

        MessageEmbed embed;
        if (firstSkillList.isEmpty()) {
            embed = new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setAuthor(event.getUser().getName(), null, event.getUser().getAvatarUrl())
                    .setTitle("스킬 등록")
                    .setDescription("등록할 수 있는 스킬이 없습니다.")
                    .build();

        } else {
            embed = buildEmbed(firstSkillList, event.getMessage().getEmbeds().get(0));
            StringSelectMenu menu = buildMenu(firstSkillList, number);
            actionRowList.add(ActionRow.of(menu));

        }

        event.getHook().editOriginalEmbeds(embed)
                .setComponents(actionRowList)
                .queue();
    }

    private MessageEmbed buildEmbed(List<SkillResponse> skillList, MessageEmbed embed) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(embed.getAuthor().getName(), null, embed.getAuthor().getIconUrl())
                .setTitle(":bookmark: 스킬 등록")
                .setDescription("등록할 스킬을 선택해 주세요.");

        for (SkillResponse skill : skillList) {
            embedBuilder.addField(skill.getName(), skill.getDescription(), false);
        }

        return embedBuilder.build();
    }

    private StringSelectMenu buildMenu(List<SkillResponse> skillList, String number) {
        return StringSelectMenu.create("skillMenu")
                .addOptions(skillEventUtil.buildOptionList(skillList, number))
                .build();
    }
}
