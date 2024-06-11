package com.htmake.htbot.domain.skill.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.skill.entity.PlayerSkill;
import com.htmake.htbot.domain.skill.entity.RegisteredSkill;
import com.htmake.htbot.domain.skill.entity.Skill;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillListResponse;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import com.htmake.htbot.domain.skill.repository.PlayerSkillRepository;
import com.htmake.htbot.domain.skill.repository.RegisteredSkillRepository;
import com.htmake.htbot.domain.skill.service.NotRegisteredSkillListService;
import com.htmake.htbot.global.annotation.ReadOnlyService;
import com.htmake.htbot.global.util.SkillUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ReadOnlyService
@RequiredArgsConstructor
public class NotRegisteredSkillListServiceImpl implements NotRegisteredSkillListService {

    private final PlayerRepository playerRepository;
    private final PlayerSkillRepository playerSkillRepository;
    private final RegisteredSkillRepository registeredSkillRepository;
    private final SkillUtil skillUtil;

    @Override
    public SkillListResponse execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        List<PlayerSkill> playerSkillList = playerSkillRepository.findByPlayer(player);
        List<RegisteredSkill> registeredSkillList = registeredSkillRepository.findByPlayer(player);

        List<SkillResponse> responseList = new ArrayList<>();

        for (PlayerSkill playerSkill : playerSkillList) {
            Skill skill = playerSkill.getSkill();

            boolean isRegistered = registeredSkillList.stream()
                    .anyMatch(registeredSkill -> skill.equals(registeredSkill.getSkill()));

            if (!isRegistered) {
                SkillResponse response = skillUtil.buildSkillResponse(skill, false);
                responseList.add(response);
            }
        }

        return SkillListResponse.builder()
                .skillResponseList(responseList)
                .build();
    }
}
