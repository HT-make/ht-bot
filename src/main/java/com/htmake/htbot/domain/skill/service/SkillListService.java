package com.htmake.htbot.domain.skill.service;

import com.htmake.htbot.domain.skill.presentation.data.response.SkillListResponse;

public interface SkillListService {

    SkillListResponse execute(String playerId);
}
