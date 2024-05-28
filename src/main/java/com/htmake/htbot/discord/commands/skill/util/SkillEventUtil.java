package com.htmake.htbot.discord.commands.skill.util;

import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SkillEventUtil {

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
}
