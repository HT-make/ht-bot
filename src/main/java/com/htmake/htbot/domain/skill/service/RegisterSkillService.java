package com.htmake.htbot.domain.skill.service;

import com.htmake.htbot.domain.skill.presentation.data.request.RegisterSkillRequest;

public interface RegisterSkillService {

    void execute(String playerId, RegisterSkillRequest request);
}
