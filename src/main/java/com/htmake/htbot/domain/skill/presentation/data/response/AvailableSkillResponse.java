package com.htmake.htbot.domain.skill.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableSkillResponse {

    private String name;

    private int value;

    private int mana;

    private String skillType;

    private String isRegistered;
}
