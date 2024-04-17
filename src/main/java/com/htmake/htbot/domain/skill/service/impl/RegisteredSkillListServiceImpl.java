package com.htmake.htbot.domain.skill.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.skill.entity.PlayerSkill;
import com.htmake.htbot.domain.skill.presentation.data.response.RegisteredSkillListResponse;
import com.htmake.htbot.domain.skill.presentation.data.response.RegisteredSkillResponse;
import com.htmake.htbot.domain.skill.repository.PlayerSkillRepository;
import com.htmake.htbot.domain.skill.service.RegisteredSkillListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegisteredSkillListServiceImpl implements RegisteredSkillListService {

    private final PlayerRepository playerRepository;
    private final PlayerSkillRepository playerSkillRepository;

    @Override
    public RegisteredSkillListResponse execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        List<PlayerSkill> playerSkillList = playerSkillRepository.findByPlayer(player);

        List<RegisteredSkillResponse> responseList = new ArrayList<>();

        for (PlayerSkill playerSkill : playerSkillList) {
            RegisteredSkillResponse response = RegisteredSkillResponse.builder()
                    .number(playerSkill.getNumber())
                    .name(playerSkill.getSkill().getName())
                    .build();

            responseList.add(response);
        }

        return RegisteredSkillListResponse.builder()
                .skillResponseList(responseList)
                .build();
    }
}
