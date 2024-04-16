package com.htmake.htbot.domain.skill.service;

import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillListResponse;

public interface NotRegisteredSkillListService {

    AvailableSkillListResponse execute(String playerId);
}
