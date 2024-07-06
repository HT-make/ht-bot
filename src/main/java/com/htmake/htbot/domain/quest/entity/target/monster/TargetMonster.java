package com.htmake.htbot.domain.quest.entity.target.monster;

import com.htmake.htbot.domain.monster.entity.Monster;
import com.htmake.htbot.domain.quest.entity.MainQuest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetMonster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "target_monster_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "monster_id")
    private Monster monster;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "main_quest_id")
    private MainQuest mainQuest;

    @Column(name = "required_monster_quantity", nullable = false)
    private int requiredQuantity;
}
