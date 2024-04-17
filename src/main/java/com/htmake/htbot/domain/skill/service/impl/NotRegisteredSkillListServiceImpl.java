package com.htmake.htbot.domain.skill.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.skill.entity.PlayerSkill;
import com.htmake.htbot.domain.skill.entity.Skill;
import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillListResponse;
import com.htmake.htbot.domain.skill.presentation.data.response.AvailableSkillResponse;
import com.htmake.htbot.domain.skill.repository.PlayerSkillRepository;
import com.htmake.htbot.domain.skill.repository.SkillRepository;
import com.htmake.htbot.domain.skill.service.NotRegisteredSkillListService;
import com.htmake.htbot.global.util.SkillUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotRegisteredSkillListServiceImpl implements NotRegisteredSkillListService {

    private final SkillRepository skillRepository;
    private final PlayerRepository playerRepository;
    private final PlayerSkillRepository playerSkillRepository;
    private final SkillUtil skillUtil;

    @Override
    public AvailableSkillListResponse execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        List<Skill> skillList = skillRepository.findByJob(player.getJob());
        List<PlayerSkill> playerSkillList = playerSkillRepository.findByPlayer(player);

        List<AvailableSkillResponse> responseList = new ArrayList<>();

        for (Skill skill : skillList) {
            boolean isRegistered = playerSkillList.stream()
                    .anyMatch(playerSkill -> skill.equals(playerSkill.getSkill()));

            if (!isRegistered) {
                AvailableSkillResponse response = skillUtil.buildAvailableSkillResponse(skill, false);
                responseList.add(response);
            }
        }

        return AvailableSkillListResponse.builder()
                .skillResponseList(responseList)
                .build();
    }
}
