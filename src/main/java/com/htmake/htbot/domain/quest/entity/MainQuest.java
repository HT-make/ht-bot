package com.htmake.htbot.domain.quest.entity;

import com.htmake.htbot.domain.misc.entity.Misc;
import com.htmake.htbot.domain.monster.entity.Monster;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainQuest {
    @Id
    @Column(name = "main_quest_id")
    private Long id;

    @Column(name = "quest_title")
    private String title;

    @Column(name = "quest_description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "target_monster_id")
    private Monster targetMonster;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "target_item_id")
    private Misc targetItem;

    @Column(name = "target_monster_quantity")
    private int targetMonsterQuantity;

    @Column(name = "target_item_quantity")
    private int targetItemQuantity;

    @Column(name = "quest_gold")
    private int gold;

    @Column(name = "quest_exp")
    private int exp;

    @Column(name = "reward_item_id")
    private String rewardItemId;

    @Column(name = "reward_item_name")
    private String rewardItemName;

    @Column(name = "reward_item_quantity")
    private int rewardItemQuantity;

    @OneToMany(mappedBy = "mainQuest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlayerQuest> playerQuest;
}
