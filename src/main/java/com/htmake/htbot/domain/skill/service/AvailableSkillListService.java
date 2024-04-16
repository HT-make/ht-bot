package com.htmake.htbot.domain.skill.service;

import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillListResponse;

public interface AvailableSkillListService {

    AvailableSkillListResponse execute(String playerId);
}
