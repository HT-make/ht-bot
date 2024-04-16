package com.htmake.htbot.domain.skill.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredSkillListResponse {

    List<RegisteredSkillResponse> skillResponseList;
}
