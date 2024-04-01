package com.htmake.htbot.domain.player.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Status {

    @Id
    @Column(name = "player_id")
    private String id;

    @Column(name = "player_damage", nullable = false)
    private int damage;

    @Column(name = "player_health", nullable = false)
    private int health;

    @Column(name = "player_defence", nullable = false)
    private int defence;

    @Column(name = "player_mana", nullable = false)
    private int mana;

    @Column(name = "player_criticalChance", nullable = false)
    private int criticalChance;

    @Column(name = "player_criticalDamage", nullable = false)
    private int criticalDamage;
}
