package com.htmake.htbot.discord.commands.skill.util;

import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SkillEventUtil {

    public java.util.List<AvailableSkillResponse> toSkillList(JSONArray skillArray) {
        List<AvailableSkillResponse> skillList = new ArrayList<>();

        for (int i = 0; i < skillArray.length(); i++) {
            JSONObject skillObject = skillArray.getJSONObject(i);

            AvailableSkillResponse skill = AvailableSkillResponse.builder()
                    .id(skillObject.getLong("id"))
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

    public String format(AvailableSkillResponse skill) {
        String skillType = skill.getSkillType().equals("ATTACK") ? "데미지" : "치유량";
        return String.format("%s: %d%% 마나: %d", skillType, skill.getValue(), skill.getMana());
    }
}
