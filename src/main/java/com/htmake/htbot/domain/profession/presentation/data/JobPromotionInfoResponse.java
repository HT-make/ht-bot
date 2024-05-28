package com.htmake.htbot.domain.profession.presentation.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobPromotionInfoResponse {

    private int level;

    private int gold;

    private int gem;

    private String nextJob;

    private String nextJobName;

    private String itemName;

    private int itemQuantity;

    private String description;

    private int requiredLevel;

    private int requiredGold;

    private int requiredGem;

    private int requiredItemQuantity;
}
