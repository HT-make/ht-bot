package com.htmake.htbot.domain.skill.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.skill.entity.RegisteredSkill;
import com.htmake.htbot.domain.skill.presentation.data.response.RegisteredSkillListResponse;
import com.htmake.htbot.domain.skill.presentation.data.response.RegisteredSkillResponse;
import com.htmake.htbot.domain.skill.repository.RegisteredSkillRepository;
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
    private final RegisteredSkillRepository registeredSkillRepository;

    @Override
    public RegisteredSkillListResponse execute(String playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        List<RegisteredSkill> registeredSkillList = registeredSkillRepository.findByPlayer(player);

        List<RegisteredSkillResponse> responseList = new ArrayList<>();

        for (RegisteredSkill registeredSkill : registeredSkillList) {
            RegisteredSkillResponse response = RegisteredSkillResponse.builder()
                    .number(registeredSkill.getNumber())
                    .name(registeredSkill.getSkill().getName())
                    .build();

            responseList.add(response);
        }

        return RegisteredSkillListResponse.builder()
                .skillResponseList(responseList)
                .build();
    }
}
