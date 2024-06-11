package com.htmake.htbot.domain.skill.service.impl;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.exception.NotFoundPlayerException;
import com.htmake.htbot.domain.player.repository.PlayerRepository;
import com.htmake.htbot.domain.skill.entity.RegisteredSkill;
import com.htmake.htbot.domain.skill.entity.Skill;
import com.htmake.htbot.domain.skill.exception.SkillNotFoundException;
import com.htmake.htbot.domain.skill.presentation.data.request.RegisterSkillRequest;
import com.htmake.htbot.domain.skill.repository.RegisteredSkillRepository;
import com.htmake.htbot.domain.skill.repository.SkillRepository;
import com.htmake.htbot.domain.skill.service.RegisterSkillService;
import com.htmake.htbot.global.annotation.TransactionalService;
import lombok.RequiredArgsConstructor;

@TransactionalService
@RequiredArgsConstructor
public class RegisterSkillServiceImpl implements RegisterSkillService {

    private final PlayerRepository playerRepository;
    private final SkillRepository skillRepository;
    private final RegisteredSkillRepository registeredSkillRepository;

    @Override
    public void execute(String playerId, RegisterSkillRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(NotFoundPlayerException::new);

        Skill skill = skillRepository.findById(request.getId())
                .orElseThrow(SkillNotFoundException::new);

        int number = request.getNumber();

        RegisteredSkill RegisteredSkill = registeredSkillRepository.findByNumberAndPlayer(number, player)
                .orElse(null);

        if (RegisteredSkill != null) {
            RegisteredSkill.setSkill(skill);
            registeredSkillRepository.save(RegisteredSkill);
        } else {
            RegisteredSkill newRegisterSkill = RegisteredSkill.builder()
                    .number(number)
                    .player(player)
                    .skill(skill)
                    .build();

            registeredSkillRepository.save(newRegisterSkill);
        }
    }
}
