package com.htmake.htbot.discord.commands.skill.util;

import com.htmake.htbot.discord.commands.skill.cache.SkillCache;
import com.htmake.htbot.discord.commands.skill.data.SkillList;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import com.htmake.htbot.global.cache.CacheFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SkillEventUtil {

    private final SkillCache skillCache;

    public SkillEventUtil() {
        this.skillCache = CacheFactory.skillCache;
    }

    public java.util.List<SkillResponse> toSkillList(JSONArray skillArray) {
        List<SkillResponse> skillList = new ArrayList<>();

        for (int i = 0; i < skillArray.length(); i++) {
            JSONObject skillObject = skillArray.getJSONObject(i);

            SkillResponse skill = SkillResponse.builder()
                    .id(skillObject.getString("id"))
                    .name(skillObject.getString("name"))
                    .description(skillObject.getString("description"))
                    .isRegistered(skillObject.getString("isRegistered"))
                    .build();

            skillList.add(skill);
        }

        return skillList;
    }

    public MessageEmbed skillListBuildEmbed(List<SkillResponse> skillList, User user) {
        String profileUrl = user.getAvatarUrl() != null ? user.getAvatarUrl() : user.getDefaultAvatarUrl();

        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Color.GREEN)
                .setAuthor(user.getName(), null, profileUrl)
                .setTitle(":bookmark: 스킬 목록");

        for (SkillResponse skill : skillList) {
            String title = skill.getName();

            if (skill.getIsRegistered().equals("true")) {
                title += "(등록됨)";
            }

            embedBuilder.addField(title, skill.getDescription(), false);
        }

        return embedBuilder.build();
    }
    public void saveSkillListCache (List<SkillResponse> skillList, User user) {
        List<SkillResponse> firstSkillList = new ArrayList<>();
        List<SkillResponse> secondSkillList = new ArrayList<>();
        List<SkillResponse> thirdSkillList = new ArrayList<>();

        for (SkillResponse skill : skillList) {
            String skillId = skill.getId();
            char thirdCharacter = skillId.charAt(2);
            if (thirdCharacter == '1') {
                firstSkillList.add(skill);
            } else if (thirdCharacter == '2') {
                secondSkillList.add(skill);
            } else {
                thirdSkillList.add(skill);
            }
        }

        SkillList skillLists = SkillList.builder()
                .firstSkillList(firstSkillList)
                .secondSkillList(secondSkillList)
                .thirdSkillList(thirdSkillList)
                .build();

        skillCache.put(user.getId(), skillLists);
    }

    public List<SelectOption> buildOptionList(List<SkillResponse> skillList, String number) {
        List<SelectOption> optionList = new ArrayList<>();

        for (SkillResponse skill : skillList) {
            String value = String.format("skill-register-%s-%s", skill.getId(), number);
            SelectOption option = SelectOption.of(skill.getName(), value);
            optionList.add(option);
        }

        return optionList;
    }

    public List<Button> skillButtonEmbed(int page, String number, String prefix) {
        List<Button> buttonList = new ArrayList<>();

        Button firstButton = Button.primary(prefix + "-1-" + number, "1차").asEnabled();
        Button secondButton = Button.primary(prefix + "-2-" + number, "2차").asEnabled();
        Button thirdButton = Button.primary(prefix + "-3-" + number, "3차").asEnabled();
        Button cancelButton = Button.danger("cancel", "닫기");

        switch (page) {
            case 1 -> firstButton = firstButton.asDisabled();
            case 2 -> secondButton = secondButton.asDisabled();
            case 3 -> thirdButton = thirdButton.asDisabled();
        }

        buttonList.add(firstButton);
        buttonList.add(secondButton);
        buttonList.add(thirdButton);
        buttonList.add(cancelButton);

        return buttonList;
    }
}
