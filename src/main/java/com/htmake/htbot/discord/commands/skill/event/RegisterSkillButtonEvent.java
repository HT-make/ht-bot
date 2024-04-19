package com.htmake.htbot.discord.commands.skill.event;

import com.htmake.htbot.discord.commands.skill.util.SkillEventUtil;
import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillResponse;
import com.htmake.htbot.global.unirest.HttpClient;
import com.htmake.htbot.global.unirest.impl.HttpClientImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import kotlin.Pair;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.json.JSONArray;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegisterSkillButtonEvent {

    private final HttpClient httpClient;
    private final SkillEventUtil skillEventUtil;

    public RegisterSkillButtonEvent() {
        this.httpClient = new HttpClientImpl();
        this.skillEventUtil = new SkillEventUtil();
    }

    public void execute(ButtonInteractionEvent event, String number) {
        HttpResponse<JsonNode> response = request(event.getUser().getId());

        if (response.getStatus() == 200) {
            JSONArray skillArray = response.getBody().getObject().getJSONArray("skillResponseList");
            requestSuccess(event, skillArray, number);
        } else {
            skillEventUtil.errorMessage(event, "스킬 등록", "스킬 목록을 불러오지 못했습니다.");
        }
    }

    private HttpResponse<JsonNode> request(String playerId) {
        String endPoint = "/skill/not/registered/{player_id}";
        Pair<String, String> routeParam = new Pair<>("player_id", playerId);
        return httpClient.sendGetRequest(endPoint, routeParam);
    }

    private void requestSuccess(ButtonInteractionEvent event, JSONArray skillArray, String number) {
        List<AvailableSkillResponse> skillList = skillEventUtil.toSkillList(skillArray);
        MessageEmbed embed = buildEmbed(skillList);
        StringSelectMenu menu = buildMenu(skillList, number);

        event.getMessage().editMessageEmbeds(embed)
                .setActionRow(menu)
                .queue();
    }

    private MessageEmbed buildEmbed(List<AvailableSkillResponse> skillList) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(":bookmark: 스킬 등록")
                .setDescription("등록할 스킬을 선택해 주세요.");

        for (AvailableSkillResponse skill : skillList) {
            embedBuilder.addField(skill.getName(), skillEventUtil.format(skill), false);
        }

        return embedBuilder.build();
    }

    private StringSelectMenu buildMenu(List<AvailableSkillResponse> skillList, String number) {
        return StringSelectMenu.create("skillMenu")
                .addOptions(buildOptionList(skillList, number))
                .build();
    }

    private List<SelectOption> buildOptionList(List<AvailableSkillResponse> skillList, String number) {
        List<SelectOption> optionList = new ArrayList<>();

        for (AvailableSkillResponse skill : skillList) {
            String value = String.format("skill-register-%d-%s", skill.getId(), number);
            SelectOption option = SelectOption.of(skill.getName(), value);
            optionList.add(option);
        }

        return optionList;
    }
}
