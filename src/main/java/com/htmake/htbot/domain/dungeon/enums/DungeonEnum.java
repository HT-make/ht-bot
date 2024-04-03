package com.htmake.htbot.domain.dungeon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DungeonEnum {

    DUNGEON_ONE("드넓은 초원", "dungeon1"),
    DUNGEON_TWO("깊은 동굴", "dungeon2"),
    DUNGEON_THREE("끈적이는 늪", "dungeon3"),
    DUNGEON_FOUR("어두운 숲", "dungeon4"),
    DUNGEON_FIVE("몰락한 성", "dungeon5"),
    DUNGEON_SIX("용암 지대", "dungeon6");

    private final String name;
    private final String value;
}
