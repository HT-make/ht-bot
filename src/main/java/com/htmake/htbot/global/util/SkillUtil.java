package com.htmake.htbot.global.util;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.skill.entity.PlayerSkill;
import com.htmake.htbot.domain.skill.entity.Skill;
import com.htmake.htbot.domain.skill.presentation.data.response.SkillResponse;
import com.htmake.htbot.domain.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SkillUtil {

    private final SkillRepository skillRepository;

    public SkillResponse buildSkillResponse(Skill skill, boolean isRegistered) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .description(skill.getDescription())
                .isRegistered(String.valueOf(isRegistered))
                .build();
    }

    public List<PlayerSkill> buildPlayerSkillList(Player player, Job job) {
        List<Skill> skillList = skillRepository.findByJob(job);
        List<PlayerSkill> playerSkillList = new ArrayList<>();

        skillList.forEach(skill -> {
            PlayerSkill playerSkill = PlayerSkill.builder()
                    .player(player)
                    .skill(skill)
                    .build();

            playerSkillList.add(playerSkill);
        });

        return playerSkillList;
    }
}
