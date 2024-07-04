package com.htmake.htbot.domain.skill.repository;

import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.skill.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, String> {

    List<Skill> findByJob(Job job);
}
