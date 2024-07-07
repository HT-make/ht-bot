package com.htmake.htbot.domain.quest.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerQuestInfoResponse {

    private String title;

    private String description;

    private int exp;

    private int gold;

    private List<TargetResponse> targetList;

    private List<RewardResponse> rewardList;
}
