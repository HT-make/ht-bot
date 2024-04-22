package com.htmake.htbot.domain.player.entity;

import com.htmake.htbot.domain.inventory.entity.Inventory;
import com.htmake.htbot.domain.player.enums.Job;
import com.htmake.htbot.domain.quest.entity.Quest;
import com.htmake.htbot.domain.skill.entity.PlayerSkill;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {

    @Id
    @Column(name = "player_id")
    private String id;

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

    @Column(name = "job")
    @Enumerated(EnumType.STRING)
    private Job job;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<Inventory> inventoryList;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<PlayerSkill> playerSkills;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private Quest quest;

    public void killMonster(int currentExp, int gold) {
        this.currentExp = currentExp;
        this.gold = gold;
    }

    public void levelUp(int maxExp) {
        this.maxExp = maxExp;
        this.level++;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
