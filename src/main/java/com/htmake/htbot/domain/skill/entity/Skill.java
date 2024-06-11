package com.htmake.htbot.domain.skill.entity;

import com.htmake.htbot.domain.player.enums.Job;
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
    @Column(name = "skill_id")
    private String id;

    @Column(name = "skill_name", unique = true, nullable = false)
    private String name;

    @Column(name = "skill_description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_job", nullable = false)
    private Job job;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<PlayerSkill> playerSkillList;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<RegisteredSkill> registeredSkillList;
}
