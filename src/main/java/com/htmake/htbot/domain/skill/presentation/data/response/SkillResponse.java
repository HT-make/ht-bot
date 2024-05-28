package com.htmake.htbot.domain.skill.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkillResponse {

    private String id;

    private String name;

    private String description;

    private String isRegistered;
}
