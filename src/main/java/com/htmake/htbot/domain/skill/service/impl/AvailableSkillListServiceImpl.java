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
import com.htmake.htbot.domain.skill.service.AvailableSkillListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AvailableSkillListServiceImpl implements AvailableSkillListService {

    private final SkillRepository skillRepository;
    private final PlayerRepository playerRepository;
    private final PlayerSkillRepository playerSkillRepository;

    @Override
    public AvailableSkillListResponse execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        List<Skill> skillList = skillRepository.findByJob(player.getJob());
        List<PlayerSkill> playerSkillList = playerSkillRepository.findByPlayer(player);

        List<AvailableSkillResponse> responseList = new ArrayList<>();

        for (Skill skill : skillList) {
            String isRegistered = "false";

            for (PlayerSkill playerSkill : playerSkillList) {
                if (skill.equals(playerSkill.getSkill())) {
                    isRegistered = "true";
                    break;
                }
            }

            AvailableSkillResponse response = AvailableSkillResponse.builder()
                    .name(skill.getName())
                    .value(skill.getValue())
                    .mana(skill.getMana())
                    .skillType(skill.getSkillType().name())
                    .isRegistered(isRegistered)
                    .build();

            responseList.add(response);
        }

        return AvailableSkillListResponse.builder()
                .skillResponseList(responseList)
                .build();
    }
}
