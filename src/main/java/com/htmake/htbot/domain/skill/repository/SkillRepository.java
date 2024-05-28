package com.htmake.htbot.domain.skill.repository;

import com.htmake.htbot.domain.skill.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, String> {
}
