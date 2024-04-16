package com.htmake.htbot.domain.skill.repository;

import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.skill.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByJob(Job job);

    Optional<Skill> findByName(String name);
}
