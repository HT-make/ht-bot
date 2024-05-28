package com.htmake.htbot.global.util;

import com.htmake.htbot.domain.skill.entity.Skill;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import org.springframework.stereotype.Component;

@Component
public class SkillUtil {

    public SkillResponse buildSkillResponse(Skill skill, boolean isRegistered) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .description(skill.getDescription())
                .isRegistered(String.valueOf(isRegistered))
                .build();
    }
}
