package com.htmake.htbot.domain.quest.entity.reward;

import com.htmake.htbot.domain.quest.entity.MainQuest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_reward_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "main_quest_id")
    private MainQuest mainQuest;

    @Column(name = "reward_item_id", nullable = false)
    private String itemId;

    @Column(name = "reward_item_name", nullable = false)
    private String itemName;

    @Column(name = "reward_item_quantity", nullable = false)
    private int itemQuantity;
}
