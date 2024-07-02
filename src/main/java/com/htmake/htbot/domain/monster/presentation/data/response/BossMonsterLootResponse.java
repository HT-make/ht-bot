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
public class BossMonsterLootResponse {

    private int exp;

    private int gold;

    private int gem;

    private int bossCoin;

    private List<DropItemResponse> dropItemList;
}
