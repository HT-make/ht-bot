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
public class Player {

    @Id
    @Column(name = "player_id")
    private String id;

    @Column(name = "player_name", nullable = false)
    private String name;

    @Column(name = "player_level", nullable = false)
    private int level;

    @Column(name = "player_current_exp", nullable = false)
    private int currentExp;

    @Column(name = "player_max_exp", nullable = false)
    private int maxExp;

    @Column(name = "player_gold", nullable = false)
    private int gold;

    @Column(name = "player_gem", nullable = false)
    private int gem;

    public void killMonster(int currentExp, int gold) {
        this.currentExp = currentExp;
        this.gold = gold;
    }

    public void levelUp(int maxExp) {
        this.maxExp = maxExp;
        this.level++;
    }
}
