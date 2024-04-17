package com.htmake.htbot.domain.quest.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerQuestInfoResponse {
    private String title;
    private String description;
    private String targetMonster;
    private String targetItem;
    private int targetMonsterQuantity;
    private int targetItemQuantity;
    private int gold;
    private int exp;
    private String rewardItemId;
    private String rewardItemName;
    private int rewardItemQuantity;
    private int itemQuantity;
    private int monsterQuantity;
}
