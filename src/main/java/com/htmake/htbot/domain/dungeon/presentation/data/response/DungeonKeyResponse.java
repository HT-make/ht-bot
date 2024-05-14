package com.htmake.htbot.domain.dungeon.presentation.data.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DungeonKeyResponse {

    private String name;

    private int requireQuantity;

    private int playerQuantity;
}
