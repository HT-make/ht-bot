package com.htmake.htbot.domain.monster.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonsterSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monster_skill_id")
    private Long id;

    @Column(name = "monster_skill_name", nullable = false)
    private String name;

    @Column(name = "monster_skill_damage", nullable = false)
    private int damage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monster_id")
    private Monster monster;
}
