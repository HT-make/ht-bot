package com.htmake.htbot.domain.monster.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonsterLootResponse {

    private int exp;

    private int gold;

    private List<DropItemResponse> dropItemList;
}
