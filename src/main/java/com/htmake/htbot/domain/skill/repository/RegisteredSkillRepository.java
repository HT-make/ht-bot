package com.htmake.htbot.domain.skill.repository;

import com.htmake.htbot.domain.player.entity.Player;
import com.htmake.htbot.domain.skill.entity.RegisteredSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegisteredSkillRepository extends JpaRepository<RegisteredSkill, Long> {

    List<RegisteredSkill> findByPlayer(Player player);

    Optional<RegisteredSkill> findByNumberAndPlayer(int number, Player player);
}
