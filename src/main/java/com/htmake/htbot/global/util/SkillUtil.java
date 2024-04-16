package com.htmake.htbot.global.util;

import com.htmake.htbot.domain.skill.entity.Skill;
import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillResponse;
import org.springframework.stereotype.Component;

@Component
public class SkillUtil {

    public AvailableSkillResponse buildAvailableSkillResponse(Skill skill, boolean isRegistered) {
        return AvailableSkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .value(skill.getValue())
                .mana(skill.getMana())
                .skillType(skill.getSkillType().name())
                .isRegistered(String.valueOf(isRegistered))
                .build();
    }
}
