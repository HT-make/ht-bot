package com.htmake.htbot.domain.skill.repository;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.skill.entity.PlayerSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerSkillRepository extends JpaRepository<PlayerSkill, Long> {

    List<PlayerSkill> findByPlayer(Player player);

    PlayerSkill findByNumberAndPlayer(int number, Player player);
}
