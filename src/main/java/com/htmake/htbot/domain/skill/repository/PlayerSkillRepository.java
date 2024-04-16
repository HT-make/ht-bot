package com.htmake.htbot.domain.skill.repository;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.skill.entity.PlayerSkill;
import com.htmake.htbot.domain.skill.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerSkillRepository extends JpaRepository<PlayerSkill, Long> {

    boolean existsByPlayerAndSkill(Player player, Skill skill);

    List<PlayerSkill> findByPlayer(Player player);
}