package com.htmake.htbot.domain.player.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerSkillResponse {

    private int number;

    private String name;

    private int value;

    private int mana;

    private String skillType;
}
