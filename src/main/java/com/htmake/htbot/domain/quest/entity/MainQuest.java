package com.htmake.htbot.domain.quest.entity;

import com.htmake.htbot.domain.quest.entity.reward.QuestReward;
import com.htmake.htbot.domain.quest.entity.dialogue.QuestDialogue;
import com.htmake.htbot.domain.quest.entity.target.misc.TargetMisc;
import com.htmake.htbot.domain.quest.entity.target.monster.TargetMonster;
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

    @Column(name = "quest_title", nullable = false)
    private String title;

    @Column(name = "quest_description", nullable = false)
    private String description;

    @Column(name = "quest_gold", nullable = false)
    private int gold;

    @Column(name = "quest_exp", nullable = false)
    private int exp;

    @OneToMany(mappedBy = "mainQuest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestReward> questRewardList;

    @OneToMany(mappedBy = "mainQuest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TargetMonster> targetMonsterList;

    @OneToMany(mappedBy = "mainQuest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TargetMisc> targetMiscList;

    @OneToMany(mappedBy = "mainQuest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlayerQuest> playerQuestList;

    @OneToMany(mappedBy = "mainQuest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestDialogue> questDialogueList;
}
