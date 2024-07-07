package com.htmake.htbot.domain.quest.presentation.data.response;

import com.htmake.htbot.domain.quest.entity.reward.QuestReward;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RewardResponse {

    private String name;

    private int quantity;

    public static RewardResponse toResponse(QuestReward questReward) {
        return RewardResponse.builder()
                .name(questReward.getItemName())
                .quantity(questReward.getItemQuantity())
                .build();
    }
}
