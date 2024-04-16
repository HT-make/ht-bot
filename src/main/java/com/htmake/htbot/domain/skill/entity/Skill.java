package com.htmake.htbot.domain.skill.entity;

import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.skill.enums.SkillType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long id;

    @Column(name = "skill_name", unique = true, nullable = false)
    private String name;

    @Column(name = "skill_value", nullable = false)
    private int value;

    @Column(name = "skill_mana", nullable = false)
    private int mana;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_type", nullable = false)
    private SkillType skillType;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_job", nullable = false)
    private Job job;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<PlayerSkill> playerSkills;
}
