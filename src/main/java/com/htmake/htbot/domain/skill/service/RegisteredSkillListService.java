package com.htmake.htbot.domain.skill.service;

import com.htmake.htbot.domain.skill.presentation.data.response.RegisteredSkillListResponse;

public interface RegisteredSkillListService {

    RegisteredSkillListResponse execute(String playerId);
}
