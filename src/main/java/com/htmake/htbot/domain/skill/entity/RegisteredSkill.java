package com.htmake.htbot.domain.skill.entity;

import com.htmake.htbot.domain.player.entity.Player;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisteredSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registered_skill_id")
    private Long id;

    @Column(name = "registered_skill_number", nullable = false)
    private int number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "skill_id")
    private Skill skill;

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
